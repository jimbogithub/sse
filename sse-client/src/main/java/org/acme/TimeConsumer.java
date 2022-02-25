package org.acme;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:" + port + "/time/subscribe");
		try (SseEventSource source = SseEventSource.target(target).build()) {
			source.register(this::onEvent, this::onError, this::onComplete);
			source.open();
			latch.await(20, TimeUnit.SECONDS);
		}
		return 0;
	}

	private void onEvent(InboundSseEvent event) {
		LOG.info(event.readData());
		latch.countDown();
	}

	private void onError(Throwable t) {
		LOG.info("#onError", t);
	}

	private void onComplete() {
		LOG.info("#onComplete");
	}

}
