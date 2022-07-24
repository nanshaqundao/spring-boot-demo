package com.demo;

import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class Demo {
    public static void main(String[] args) {
        Demo.range();
    }

    public static void range(){
        Flux<Integer> flux = Flux.range(1,10); // build a producer of flux - note, no actual data produced at the momment

        Subscriber<Integer> subscriber = new BaseSubscriber<Integer>() {
        // build a subscriber
            protected void hookOnNext(Integer value) {
                System.out.println(Thread.currentThread().getName()+" -> " + value);
                request(1);
            }
        };

        flux.subscribe(subscriber); //build a subscribe relationship -> producer starts to produce and pass data to subscriber
    }
}
