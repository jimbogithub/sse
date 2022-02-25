# Overview

Quarkus based SSE Servers and SSE Clients both for reactive and non-reactive.

- Non-reactive server on port 8081.
- Reactive server on port 8082.

Clients can be directed to either server via `sse.server.port` setting in `application.properties`.

Servers broadcast a timestamp once per second to any subscribed client.

Clients subscribe for a maximum of 20 seconds or until they've received 10 events.

Usual `mvn quarkus:dev` to run any component.

See `/* FIXME ### ... */` comments for observed issues.
