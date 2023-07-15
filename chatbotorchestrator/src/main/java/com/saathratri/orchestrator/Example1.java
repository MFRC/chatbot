package com.saathratri.orchestrator;

import reactor.core.publisher.Mono;

public class Example1 {
    public static void main(String [] args) {
        Mono<String> mono = Mono.empty(); // Replace with your actual Mono instance

        Mono<Boolean> hasElementMono = mono.hasElement();
        hasElementMono.subscribe(
            hasElements -> {
                if(hasElements) { 
                    mono.subscribe(monoString -> {
                        System.out.println("monoString: " + monoString);
                    });
                } else {
                    System.out.println("Mono is empty.");
                }
            }
        );
    }
}