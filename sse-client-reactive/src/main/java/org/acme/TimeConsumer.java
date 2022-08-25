package org.acme;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.common.jaxrs.ConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class TimeConsumer implements QuarkusApplication {

	private static final Logger LOG = LoggerFactory.getLogger(TimeConsumer.class);

	private final CountDownLatch latch = new CountDownLatch(10);

	@ConfigProperty(name = "sse.server.port")
	int port;

	@Override
	public int run(String... args) throws Exception {
		/*
		 * FIXME ### Reactive ClientBuilder.newBuilder() requires withConfig(new ConfigurationImpl(RuntimeType.CLIENT)).
		 * This is not necessary for ClientBuilder.newClient() and is not necessary in either case for non-reactive. As
		 * ConfigurationImpl is a jboss.resteasy specific class this seems unlikely to be a deliberate requirement. ###
		 */
		Client client = ClientBuilder.newBuilder().withConfig(new ConfigurationImpl(RuntimeType.CLIENT))
				.register(new ClientRequestLogger()).register(new ClientResponseLogger()).build();
		WebTarget target = client.target("http://localhost:" + port + "/time/subscribe");
		try (SseEventSource source = SseEventSource.target(target).build()) {
			source.register(this::onEvent, this::onError, this::onComplete);
			source.open();
			latch.await();
		}
		return 0;
	}

	private void onEvent(InboundSseEvent event) {
		LOG.info(event.readData(Event.class).message().substring(0, 20) + "...");
//		latch.countDown();
	}

	private void onError(Throwable t) {
		LOG.info("#onError", t);
	}

	private void onComplete() {
		LOG.info("#onComplete");
	}

	record Event (String message) {
	}

	@Priority(Priorities.USER)
	static final class ClientRequestLogger implements ClientRequestFilter {

		private static final Logger LOG = LoggerFactory.getLogger(ClientRequestLogger.class);

		/*
		 * FIXME ### ClientRequestContext.getHeaders().toString() is not useful. The non-reactive version logs the
		 * actual headers. A workaround is to log the entrySet but this would be a migration issue for anyone switching
		 * to reactive from non-reactive. Note that the ClientResponseContext (see below) does log the header values.
		 * ###
		 * 
		 * path=http://localhost:8082/time/subscribe method=GET
		 * headers=org.jboss.resteasy.reactive.client.impl.ClientRequestContextImpl$ClientRequestHeadersMap@632c287f
		 * 
		 * FIXME ### Switching to ClientRequestContext.getHeaders().entrySet() shows that the Accept=text/event-stream
		 * header is not being sent. I believe such a header is normally expected to be sent for SSE (and is for the
		 * non-reactive version). ###
		 * 
		 * path=http://localhost:8082/time/subscribe method=GET headers=[User-Agent=[Resteasy Reactive Client]]
		 */

		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			LOG.info("path={} method={} headers={}", requestContext.getUri().toURL(), requestContext.getMethod(),
					requestContext.getHeaders());
		}

	}

	@Priority(Priorities.USER)
	static final class ClientResponseLogger implements ClientResponseFilter {

		private static final Logger LOG = LoggerFactory.getLogger(ClientResponseLogger.class);

		/*
		 * FIXME ### ClientResponseContext.getStatusInfo().toString() is not useful. The non-reactive version logs the
		 * code and reason. A workaround is to log those items individually but this would be a migration issue for
		 * anyone switching to reactive from non-reactive. ###
		 *
		 * path=/time/subscribe status=org.jboss.resteasy.reactive.common.jaxrs.StatusTypeImpl@24e9d9e6
		 * headers=[Content-Type=text/event-stream,transfer-encoding=chunked]
		 */
		@Override
		public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
				throws IOException {
			LOG.info("path={} status={} headers={}", requestContext.getUri().getPath(), responseContext.getStatusInfo(),
					responseContext.getHeaders());
		}

	}

}
