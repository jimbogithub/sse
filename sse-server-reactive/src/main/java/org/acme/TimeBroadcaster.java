package org.acme;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent.Builder;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("time")
@ApplicationScoped
public class TimeBroadcaster {

	private static final Logger LOG = LoggerFactory.getLogger(TimeBroadcaster.class);

	private Builder eventBuilder;
	private SseBroadcaster sseBroadcaster;
	private long lastEventId = 0;

	/* This does work for reactive. */
	public TimeBroadcaster(@Context Sse sse) {
		LOG.info("#init {}", this);

		eventBuilder = sse.newEventBuilder();
		sseBroadcaster = sse.newBroadcaster();
		sseBroadcaster.onClose(this::onClose);
		sseBroadcaster.onError(this::onError);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::broadcast, 10, 10,
				TimeUnit.MILLISECONDS);
	}

	@GET
	@Path("subscribe")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void onSubscribe(@Context SseEventSink sseEventSink) {
		LOG.info("#onSubscribe {} {}", sseEventSink.hashCode(), this);
		sseBroadcaster.register(sseEventSink);
	}

	/* FIXME ### TRIES TO SEND TO ALREADY CLOSED SINKS ### */
	private void broadcast() {
		try {
			sseBroadcaster.broadcast(eventBuilder.name("time").id(Long.toString(lastEventId++))
					.mediaType(MediaType.APPLICATION_JSON_TYPE)
					.data("{\"message\":"
							+ "\"sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd"
							+ "sdkjninsaingineqiehifuhqefnqeriuhiruhifuisdhfihfiifnidsfgibdfibfdiubdfibdfsiufbifd\"}")
					.build());
		} catch (Exception e) {
			LOG.warn("{}", e.getMessage());
		}
	}

	/* FIXME ### NEVER GETS CALLED ### */
	private void onClose(SseEventSink sseEventSink) {
		LOG.info("#onClose {}", sseEventSink.hashCode());
	}

	private void onError(SseEventSink sseEventSink, Throwable t) {
		LOG.info("#onError {}", sseEventSink.hashCode(), t);
	}

}
