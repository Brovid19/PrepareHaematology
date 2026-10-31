// POST /api/synthesise
// Sends a guideline or paper PDF to Claude and returns draft study material in the same shape
// as content/papers/*.json: verbatim graded recommendations, topic summary pages,
// flashcards and single-best-answer questions.
//
// One handler for both hosts (Web-standard Request/Response):
//   Vercel     api/synthesise.js  -> handleSynthesise(request, process.env)
//   Cloudflare worker.js          -> handleSynthesise(request, env)
//
// Configuration (server-side environment variables; never sent to the browser):
//   ANTHROPIC_API_KEY   required. The Claude API key.
//   SYNTH_ACCESS_CODE   required. A shared code learners type in the app, so the public site
//                       cannot be used by anyone to spend the key.
//   SYNTH_MODEL         optional. Defaults to claude-opus-5-5.
//
// The response is streamed as newline-delimited JSON so long generations keep the connection
// alive: {"type":"progress","chars":N} lines, then one {"type":"result",...} or {"type":"error",...}.
import Anthropic from '@anthropic-ai/sdk';

const DEFAULT_MODEL = 'claude-opus-5-5';
const MAX_PDF_BYTES = 30 * 1024 * 1024; // API request limit is 32 MB
const CATEGORIES = ['Malignant Haematology', 'Haemostasis & Thrombosis', 'Transfusion Medicine', 'General Haematology'];

const str = { type: 'string' };
const int = { type: 'integer' };
const strList = { type: 'array', items: str };
const obj = (properties) => ({ type: 'object', properties, required: Object.keys(properties), additionalProperties: false });

export const DRAFT_SCHEMA = obj({
  paper: obj({
    title: str,
    issuingBody: str,
    year: int,
    citation: str,
    doiUrl: str,
    category: { type: 'string', enum: CATEGORIES },
    overview: str
  }),
  recommendations: {
    type: 'array',
    items: obj({ id: str, group: str, grade: str, text: str, page: int })
  },
  topics: {
    type: 'array',
    items: obj({
      id: str,
      name: str,
      pages: { type: 'array', items: int },
      why: str,
      keyPoints: strList,
      numbers: { type: 'array', items: obj({ value: str, meaning: str }) },
      recs: strList,
      pitfalls: strList,
      hook: str,
      selfTest: { type: 'array', items: obj({ q: str, a: str }) }
    })
  },
  flashcards: {
    type: 'array',
    items: obj({ topicId: str, recId: str, front: str, back: str })
  },
  questions: {
    type: 'array',
    items: obj({ topicId: str, recId: str, title: str, stem: str, options: strList, correctIndex: int, rationale: str })
  },
  warnings: strList
});

const SYSTEM_PROMPT = `You turn haematology guidelines and papers into revision material for doctors preparing for the FRCPath Haematology Part 1 written exam (MCQ/EMQ and essay papers). Learners will rely on what you write, so accuracy against the attached document matters more than anything else.

Source rules:
- Use only the attached document. Do not add facts, numbers, drug doses or recommendations from memory, even if you believe they are correct. If the document is silent on something, leave it out.
- Copy every recommendation word for word, including its grade exactly as printed (e.g. "1A", "2C"). If a recommendation has no grade, use an empty string for grade. Do not merge, shorten or paraphrase recommendations. Include all of them.
- Page numbers are the page of the PDF file, counting the first page as 1 (not the journal page number printed on the page).
- If a value is ambiguous or looks like a typesetting error (for example units that seem wrong), keep what the document says and add a note to "warnings".

Structure:
- paper: bibliographic details from the document. category is the closest of the allowed values. doiUrl is "https://doi.org/..." if a DOI is printed, otherwise an empty string. overview is 3-5 plain sentences on scope and the main messages.
- recommendations: every formal recommendation, in document order. id is short and kebab-case, built from its section (e.g. "renal-2"). group is the heading the recommendation sits under.
- topics: split the document into 4-10 topics that follow its own sections, in document order. A topic should be readable in about 3 minutes. Every recommendation id must appear in exactly one topic's recs. Within a topic:
  - why: one or two sentences on why this matters clinically or for the exam.
  - keyPoints: complete, self-contained statements that preserve all key information from that section, including thresholds, percentages, trial names and results. Wrap the single most testable phrase of each point in **double asterisks**. Use no other markup.
  - numbers: the cut-offs and statistics a candidate must remember, as value and meaning.
  - pitfalls: likely exam traps and common misreadings, each grounded in the text.
  - hook: one short mnemonic or memorable phrase.
  - selfTest: 2-3 short recall questions with answers taken from this topic.
- flashcards: 2-4 per topic. One fact per card, with the question on the front and a concise answer on the back. recId is the recommendation the card tests, or an empty string.
- questions: 1-2 single-best-answer questions per topic, each with exactly 5 plausible options and one correct answer (correctIndex counts from 0). Vary the position of the correct answer. The stem should be a short clinical or laboratory vignette where possible. rationale explains why the answer is right and why the others are wrong, citing the guideline section.
- warnings: anything a human reviewer should check, such as illegible tables, figures you could not read, or ambiguities. Use an empty list if there are none.

British English and UK units throughout.`;

function json(status, body) {
  return new Response(JSON.stringify(body), { status, headers: { 'content-type': 'application/json; charset=utf-8', 'cache-control': 'no-store' } });
}

// Constant-time comparison so the access code cannot be guessed by timing.
function sameSecret(a, b) {
  const x = new TextEncoder().encode(String(a || ''));
  const y = new TextEncoder().encode(String(b || ''));
  let diff = x.length ^ y.length;
  for (let i = 0; i < Math.max(x.length, y.length); i++) diff |= (x[i] || 0) ^ (y[i] || 0);
  return diff === 0;
}

function base64Bytes(b64) {
  const clean = b64.replace(/\s/g, '');
  return Math.floor(clean.length * 3 / 4) - (clean.endsWith('==') ? 2 : clean.endsWith('=') ? 1 : 0);
}

function documentBlock(body) {
  if (body.pdfBase64) {
    const data = String(body.pdfBase64).replace(/^data:application\/pdf;base64,/, '').replace(/\s/g, '');
    if (!/^[A-Za-z0-9+/]+=*$/.test(data)) throw Object.assign(new Error('The PDF data is not valid base64.'), { status: 400 });
    if (base64Bytes(data) > MAX_PDF_BYTES) throw Object.assign(new Error('The PDF is larger than 30 MB.'), { status: 413 });
    if (!data.startsWith('JVBER')) throw Object.assign(new Error('That file does not look like a PDF.'), { status: 400 }); // "%PDF"
    return { type: 'document', source: { type: 'base64', media_type: 'application/pdf', data }, title: String(body.fileName || 'Submitted paper').slice(0, 200) };
  }
  if (body.pdfUrl) {
    let url;
    try { url = new URL(String(body.pdfUrl)); } catch { throw Object.assign(new Error('The PDF link is not a valid URL.'), { status: 400 }); }
    if (url.protocol !== 'https:') throw Object.assign(new Error('The PDF link must start with https://'), { status: 400 });
    return { type: 'document', source: { type: 'url', url: url.toString() } };
  }
  throw Object.assign(new Error('Attach a PDF or give a link to one.'), { status: 400 });
}

// Light clean-up so the app can trust the shape even if the model drifts.
export function normaliseDraft(d) {
  const recIds = new Set();
  const recommendations = (d.recommendations || []).filter(r => r && r.text).map((r, i) => {
    let id = String(r.id || `rec-${i + 1}`).toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '') || `rec-${i + 1}`;
    while (recIds.has(id)) id += '-b';
    recIds.add(id);
    return { id, group: String(r.group || 'Recommendations'), grade: /^[12][A-D]$/.test(r.grade) ? r.grade : null, text: String(r.text), page: Math.max(1, parseInt(r.page, 10) || 1) };
  });
  const topicIds = new Set();
  const topics = (d.topics || []).filter(t => t && t.name).map((t, i) => {
    let id = String(t.id || t.name).toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '').slice(0, 48) || `topic-${i + 1}`;
    while (topicIds.has(id)) id += '-b';
    topicIds.add(id);
    return {
      id, name: String(t.name),
      pages: (t.pages || []).map(p => parseInt(p, 10)).filter(p => p > 0),
      why: String(t.why || ''), keyPoints: (t.keyPoints || []).map(String),
      numbers: (t.numbers || []).map(n => [String(n.value), String(n.meaning)]),
      recs: (t.recs || []).filter(r => recIds.has(r)),
      pitfalls: (t.pitfalls || []).map(String), hook: String(t.hook || ''),
      selfTest: (t.selfTest || []).filter(s => s.q && s.a).map(s => ({ q: String(s.q), a: String(s.a) })),
      _origId: t.id
    };
  });
  const mapTopic = id => { const t = topics.find(x => x._origId === id || x.id === id); return t ? t.id : (topics[0] && topics[0].id); };
  const flashcards = (d.flashcards || []).filter(c => c.front && c.back).map(c => ({ topicId: mapTopic(c.topicId), recId: recIds.has(c.recId) ? c.recId : null, front: String(c.front), back: String(c.back) }));
  const questions = (d.questions || []).filter(q => q.stem && Array.isArray(q.options) && q.options.length >= 2).map(q => ({
    topicId: mapTopic(q.topicId), recId: recIds.has(q.recId) ? q.recId : null, title: String(q.title || 'Question'), stem: String(q.stem),
    options: q.options.map(String), correctIndex: Math.min(q.options.length - 1, Math.max(0, parseInt(q.correctIndex, 10) || 0)), rationale: String(q.rationale || '')
  }));
  topics.forEach(t => delete t._origId);
  const p = d.paper || {};
  return {
    paper: { title: String(p.title || 'Untitled paper'), issuingBody: String(p.issuingBody || ''), year: parseInt(p.year, 10) || new Date().getFullYear(), citation: String(p.citation || ''), doiUrl: /^https:\/\//.test(p.doiUrl || '') ? p.doiUrl : '', category: CATEGORIES.includes(p.category) ? p.category : 'General Haematology', overview: String(p.overview || '') },
    recommendations, topics, flashcards, questions, warnings: (d.warnings || []).map(String)
  };
}

export async function handleSynthesise(request, env) {
  if (request.method !== 'POST') return json(405, { error: 'Use POST.' });
  if (!env.ANTHROPIC_API_KEY || !env.SYNTH_ACCESS_CODE) {
    return json(503, { error: 'Paper submission is not set up on this site. The site owner needs to set ANTHROPIC_API_KEY and SYNTH_ACCESS_CODE.' });
  }
  if (!sameSecret(request.headers.get('x-access-code'), env.SYNTH_ACCESS_CODE)) return json(401, { error: 'The access code is not correct.' });

  let body, doc;
  try {
    body = await request.json();
    doc = documentBlock(body);
  } catch (e) {
    return json(e.status || 400, { error: e.status ? e.message : 'The request could not be read.' });
  }
  const notes = String(body.notes || '').slice(0, 2000);

  const client = new Anthropic({ apiKey: env.ANTHROPIC_API_KEY });
  const model = env.SYNTH_MODEL || DEFAULT_MODEL;
  const enc = new TextEncoder();

  const out = new ReadableStream({
    async start(controller) {
      const send = obj => controller.enqueue(enc.encode(JSON.stringify(obj) + '\n'));
      send({ type: 'progress', chars: 0, stage: 'reading' });
      try {
        const stream = client.beta.messages.stream({
          model,
          max_tokens: 64000,
          // Safety classifiers can occasionally decline a request; the server retries it on
          // Anthropic's recommended fallback model instead of returning a refusal.
          betas: ['server-side-fallback-2026-07-01'],
          fallbacks: 'default',
          output_config: { effort: 'high', format: { type: 'json_schema', schema: DRAFT_SCHEMA } },
          system: SYSTEM_PROMPT,
          messages: [{
            role: 'user',
            content: [
              doc,
              { type: 'text', text: 'Create the revision material for this document.' + (notes ? `\n\nNotes from the learner (follow them only where they agree with the source rules): ${notes}` : '') }
            ]
          }]
        });
        let chars = 0, last = Date.now();
        stream.on('text', t => {
          chars += t.length;
          if (Date.now() - last > 1500) { last = Date.now(); send({ type: 'progress', chars, stage: 'writing' }); }
        });
        const msg = await stream.finalMessage();
        if (msg.stop_reason === 'refusal') throw Object.assign(new Error('Claude declined to process this document.'), { status: 422 });
        if (msg.stop_reason === 'max_tokens') throw Object.assign(new Error('The document was too long to summarise in one go. Try a shorter paper or a single chapter.'), { status: 422 });
        // If a fallback model took over, its output follows the last "fallback" block.
        const lastFallback = msg.content.map(b => b.type).lastIndexOf('fallback');
        const text = msg.content.slice(lastFallback + 1).filter(b => b.type === 'text').map(b => b.text).join('');
        let parsed;
        try { parsed = JSON.parse(text); } catch { throw Object.assign(new Error('Claude returned an incomplete answer. Please try again.'), { status: 502 }); }
        const draft = normaliseDraft(parsed);
        if (body.pdfUrl) draft.sourceUrl = String(body.pdfUrl);
        send({ type: 'result', model: msg.model, usage: { input: msg.usage.input_tokens, output: msg.usage.output_tokens }, draft });
      } catch (e) {
        let message = e.message || 'Something went wrong.';
        if (e instanceof Anthropic.RateLimitError) message = 'The Claude API is busy (rate limited). Please try again in a minute.';
        else if (e instanceof Anthropic.AuthenticationError) message = 'The server\'s Claude API key was rejected. Tell the site owner.';
        else if (e instanceof Anthropic.BadRequestError) message = 'Claude could not read this document: ' + (e.message || '').slice(0, 300);
        else if (e instanceof Anthropic.APIError) message = `Claude API error (${e.status ?? 'network'}). Please try again.`;
        send({ type: 'error', error: message });
      } finally {
        controller.close();
      }
    }
  });
  return new Response(out, { status: 200, headers: { 'content-type': 'application/x-ndjson; charset=utf-8', 'cache-control': 'no-store', 'x-accel-buffering': 'no' } });
}
