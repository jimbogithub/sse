package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*  FIXME ### THE SSE CLIENT DOES NOT WORK PROPERLY IF WE DON'T HAVE THIS. ### */
public interface Dummy {

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	void dummy();

	/*- Without this, will see:
	 
	2022-02-25 14:46:41,486 INFO  [org.acm.TimeConsumer] (sse-event-source(http://localhost:8080/time/subscribe)-thread-1) #onError: javax.ws.rs.ProcessingException: RESTEASY003145: Unable to find a MessageBodyReader of content-type text/event-stream and type class org.jboss.resteasy.plugins.providers.sse.SseEventInputImpl
	at org.jboss.resteasy.core.interception.jaxrs.ClientReaderInterceptorContext.throwReaderNotFound(ClientReaderInterceptorContext.java:47)
	at org.jboss.resteasy.core.interception.jaxrs.AbstractReaderInterceptorContext.getReader(AbstractReaderInterceptorContext.java:133)
	at org.jboss.resteasy.core.interception.jaxrs.AbstractReaderInterceptorContext.proceed(AbstractReaderInterceptorContext.java:75)
	at org.jboss.resteasy.client.jaxrs.internal.ClientResponse.readFrom(ClientResponse.java:217)
	at org.jboss.resteasy.specimpl.BuiltResponse.readEntity(BuiltResponse.java:90)
	at org.jboss.resteasy.specimpl.AbstractBuiltResponse.readEntity(AbstractBuiltResponse.java:262)
	at org.jboss.resteasy.plugins.providers.sse.client.SseEventSourceImpl$EventHandler.run(SseEventSourceImpl.java:338)
	at org.jboss.resteasy.plugins.providers.sse.client.SseEventSourceScheduler$1.run(SseEventSourceScheduler.java:92)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)	 
	 
	 */

}
