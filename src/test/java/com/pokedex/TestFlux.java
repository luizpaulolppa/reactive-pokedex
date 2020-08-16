package com.pokedex;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class TestFlux {

    @Test
    void testFlux1() {
        Flux.empty();
    }

    @Test
    void testFlux2() {
        Flux<String> flux = Flux.just("Pokedex");
        flux.subscribe(System.out::println);
    }

    @Test
    void testFlux3() {
        Flux<String> flux = Flux.just("Sharizard", "Blastoise", "Picaxu");
        flux.subscribe(System.out::println);
    }
}
