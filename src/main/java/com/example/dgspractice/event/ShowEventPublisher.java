package com.example.dgspractice.event;

import com.example.dgspractice.entity.Show;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class ShowEventPublisher {

    /*
     * directBestEffort(): a live feed. Events emitted while nobody is subscribed are dropped,
     * and a new subscriber starts from "now".
     *
     * onBackpressureBuffer() would instead buffer pre-subscription events and replay them to
     * the first subscriber to arrive — surprising for a live feed, and it leaks events between
     * tests sharing one application context.
     */
    private final Sinks.Many<Show> sink = Sinks.many().multicast().directBestEffort();

    /** Called by the mutation. Emissions are dropped when nobody is subscribed. */
    public void publish(Show show) {
        sink.tryEmitNext(show);
    }

    /** One Flux per subscriber; every subscriber receives every show. */
    public Flux<Show> showAdded() {
        return sink.asFlux();
    }
}
