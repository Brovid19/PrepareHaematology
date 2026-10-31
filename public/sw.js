// Prepare Haematology Service Worker for offline use.
// Bump CACHE_VERSION whenever the caching strategy changes; content updates are picked up
// automatically because HTML and study content are fetched network-first.
const CACHE_VERSION = 'v2';
const CACHE_NAME = `prepare-haematology-${CACHE_VERSION}`;
const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/content.js',
  '/site.webmanifest'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => cache.addAll(ASSETS_TO_CACHE))
      .then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(keys.filter((key) => key !== CACHE_NAME).map((key) => caches.delete(key))))
      .then(() => self.clients.claim())
  );
});

function putInCache(request, response) {
  if (response && response.status === 200 && response.type === 'basic') {
    const copy = response.clone();
    caches.open(CACHE_NAME).then((cache) => cache.put(request, copy));
  }
  return response;
}

self.addEventListener('fetch', (event) => {
  const request = event.request;
  if (request.method !== 'GET') return;
  const url = new URL(request.url);
  if (url.origin !== self.location.origin) return;

  // Large downloads: always from the network (fall back to cache if offline).
  if (url.pathname.startsWith('/assets/') || url.pathname.startsWith('/downloads/')) {
    event.respondWith(fetch(request).catch(() => caches.match(request)));
    return;
  }

  // Pages and study content: network-first so corrections reach learners immediately,
  // with the cached copy as the offline fallback.
  const isPage = request.mode === 'navigate' || url.pathname === '/' || url.pathname.endsWith('.html');
  const isContent = url.pathname === '/content.js' || url.pathname === '/sw.js';
  if (isPage || isContent) {
    event.respondWith(
      fetch(request)
        .then((response) => putInCache(request, response))
        .catch(() => caches.match(request).then((cached) => cached || caches.match('/index.html')))
    );
    return;
  }

  // Everything else: cache-first.
  event.respondWith(
    caches.match(request).then((cached) => cached || fetch(request).then((response) => putInCache(request, response)))
  );
});
