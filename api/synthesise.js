// Vercel Function: POST /api/synthesise (see server/synthesise.js).
import { handleSynthesise } from '../server/synthesise.js';

export async function POST(request) {
  return handleSynthesise(request, process.env);
}

export async function GET() {
  return new Response(JSON.stringify({ error: 'Use POST.' }), { status: 405, headers: { 'content-type': 'application/json' } });
}
