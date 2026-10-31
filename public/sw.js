// Prepare Haematology service worker (PWA offline support).
//
// BUILD_ID is stamped by scripts/build_web_content.py whenever the app shell or content
// changes. A new BUILD_ID makes the browser install a new worker; the page then shows an
// "Update available" banner and the learner chooses when to reload.
const BUILD_ID = 'b655493b79f1';
const SHELL_CACHE = `prepare-haematology-shell-${BUILD_ID}`;
const PDF_CACHE = 'prepare-haematology-pdfs'; // kept across updates so offline PDFs survive

const APP_SHELL = [
  '/',
  '/index.html',
  '/content.js',
  '/site.webmanifest',
  '/favicon.ico',
  '/icons/icon-192.png',
  '/icons/icon-512.png',
  '/icons/maskable-192.png',
  '/icons/maskable-512.png',
  '/icons/apple-touch-icon.png',
  '/icons/favicon-32.png'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(SHELL_CACHE).then((cache) => cache.addAll(APP_SHELL.map((url) => new Request(url, { cache: 'reload' }))))
  );
  // Do not skipWaiting here: the page asks the learner before switching versions,
  // so an in-progress mock exam is never reloaded underneath them.
});

self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'SKIP_WAITING') self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(
        keys
          .filter((key) => key.startsWith('prepare-haematology') && key !== SHELL_CACHE && key !== PDF_CACHE)
          .map((key) => caches.delete(key))
      ))
      .then(() => self.clients.claim())
  );
});

function isCacheable(response) {
  return response && response.status === 200 && response.type === 'basic';
}

async function networkFirst(request, fallbackUrl) {
  const cache = await caches.open(SHELL_CACHE);
  try {
    const response = await fetch(request);
    if (isCacheable(response)) cache.put(request, response.clone());
    return response;
  } catch (err) {
    const cached = await cache.match(request, { ignoreSearch: true });
    if (cached) return cached;
    if (fallbackUrl) {
      const fallback = await cache.match(fallbackUrl);
      if (fallback) return fallback;
    }
    throw err;
  }
}

async function cacheFirst(request, cacheName) {
  const cache = await caches.open(cacheName);
  const cached = await cache.match(request);
  if (cached) return cached;
  const response = await fetch(request);
  if (isCacheable(response)) cache.put(request, response.clone());
  return response;
}

self.addEventListener('fetch', (event) => {
  const request = event.request;
  if (request.method !== 'GET') return;
  const url = new URL(request.url);
  if (url.origin !== self.location.origin) return;

  // Pages: network-first so corrections arrive immediately; offline falls back to the shell.
  if (request.mode === 'navigate') {
    event.respondWith(networkFirst(request, '/index.html'));
    return;
  }

  // Study content: network-first, cached copy offline.
  if (url.pathname === '/content.js') {
    event.respondWith(networkFirst(request));
    return;
  }

  // Guideline PDFs: cached once opened, so they can be read offline afterwards.
  if (url.pathname.startsWith('/assets/')) {
    event.respondWith(cacheFirst(request, PDF_CACHE));
    return;
  }

  // Icons, manifest and other static files.
  event.respondWith(cacheFirst(request, SHELL_CACHE));
});
