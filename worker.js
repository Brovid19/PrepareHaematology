// Cloudflare Worker entrypoint for preparehaematology
// Serves static assets from ./public via env.ASSETS with SPA routing fallback

export default {
  async fetch(request, env) {
    try {
      // Fetch the static asset from the Cloudflare Assets binding
      const response = await env.ASSETS.fetch(request);
      
      // If the asset exists (not a 404), return it directly
      if (response && response.status !== 404) {
        return response;
      }
    } catch (err) {
      // Fall through to SPA fallback
    }

    // Single Page Application (SPA) fallback: serve index.html
    try {
      const url = new URL(request.url);
      url.pathname = '/index.html';
      const indexRequest = new Request(url.toString(), request);
      return await env.ASSETS.fetch(indexRequest);
    } catch (err) {
      return new Response("Prepare Haematology app loading...", {
        status: 200,
        headers: { "Content-Type": "text/html; charset=utf-8" }
      });
    }
  }
};
