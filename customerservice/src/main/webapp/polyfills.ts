// import 'zone.js';
// Microfrontends fails to load with new recommended zone.js import.
import 'zone.js/dist/zone';
import '@angular/localize/init';

// Fix needed for SockJS, see https://github.com/sockjs/sockjs-client/issues/439
(window as any).global = window;
