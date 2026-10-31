// Worker entrypoint for preparehaematology.
// Handles the paper-submission API, then serves static assets from ./public via env.ASSETS
// with SPA routing fallback.
import { handleSynthesise } from './server/synthesise.js';

export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    if (url.pathname === '/api/synthesise') {
      return handleSynthesise(request, env);
    }
    if (url.pathname.startsWith('/api/')) {
      return new Response(JSON.stringify({ error: 'Not found' }), { status: 404, headers: { 'content-type': 'application/json' } });
    }

    try {
      const response = await env.ASSETS.fetch(request);
      if (response && response.status !== 404) return response;
    } catch (err) {
      // Fall through to SPA fallback
    }

    // Single Page Application (SPA) fallback: serve index.html
    try {
      url.pathname = '/index.html';
      return await env.ASSETS.fetch(new Request(url.toString(), request));
    } catch (err) {
      return new Response('Prepare Haematology app loading...', {
        status: 200,
        headers: { 'Content-Type': 'text/html; charset=utf-8' }
      });
    }
  }
};
