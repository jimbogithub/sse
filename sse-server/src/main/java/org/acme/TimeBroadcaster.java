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

	/*
	 * FIXME ### CANNOT INJECT (does work for reactive version) ###
	 * FIXME ### IS CALLED TWICE (FOR ...ClientProxy AND ...Subclass) ###
	 */
	public TimeBroadcaster(/* @Context Sse sse */) {
		LOG.info("#init {}", this);
//		setSse(sse);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::broadcast, 1, 1, TimeUnit.SECONDS);
	}

	/* FIXME ### HAVE BEEN UNABLE TO GET A SENSIBLE LIFECYCLE WORKING. ### */
	@Context
	public void setSse(Sse sse) {
		LOG.info("#setSse {}", this);

		eventBuilder = sse.newEventBuilder();
		sseBroadcaster = sse.newBroadcaster();
		sseBroadcaster.onClose(this::onClose);
		sseBroadcaster.onError(this::onError);
	}

	@GET
	@Path("subscribe")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void onSubscribe(@Context SseEventSink sseEventSink) {
		LOG.info("#onSubscribe {} {}", sseEventSink.hashCode(), this);
		sseBroadcaster.register(sseEventSink);
	}

	private void broadcast() {
		try {
			sseBroadcaster.broadcast(eventBuilder.name("time").id(Long.toString(lastEventId++))
					.mediaType(MediaType.TEXT_PLAIN_TYPE).data(Instant.now().toString()).build());
		} catch (Exception e) {
			/* FIXME ### HITS THIS DUE TO SECOND CLASS INSTANCE WITH NO SSE CONTEXT. ### */
			LOG.warn("#broadcast {}", e.getMessage());
		}
	}

	private void onClose(SseEventSink sseEventSink) {
		LOG.info("#onClose {}", sseEventSink.hashCode());
	}

	/* FIXME ### GETS CALLED (TWICE) WHEN CLIENT HAS SIMPLY CLOSED CLEANLY ### */
	private void onError(SseEventSink sseEventSink, Throwable t) {
		LOG.info("#onError {}", sseEventSink.hashCode(), t);
	}

}
