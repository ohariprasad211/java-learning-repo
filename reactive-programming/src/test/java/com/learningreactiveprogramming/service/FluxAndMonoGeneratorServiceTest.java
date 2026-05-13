package com.learningreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        //then
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        //given
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_Map();
        //then
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }
    @Test
    void namesFlux_Immutability() {
        //given
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_Immutability();
        //then
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }


    @Test
    void namesFlux_FlatMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_FlatMap(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C","H", "L", "O", "E")
                .verifyComplete();
    }
    @Test
    void namesFlux_FlatMap_withDelay() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_FlatMap_async(3);
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C","H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap_withDelay() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C","H", "L", "O", "E")
                .verifyComplete();
    }
    @Test
    void namesMono_flatMap() {
        var namesFlux = fluxAndMonoGeneratorService.namesMono_flatMap(3);
        StepVerifier.create(namesFlux)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }
    @Test
    void namesMono_flatMapMany() {
        var namesFlux = fluxAndMonoGeneratorService.namesMono_flatMapMany(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFlux_Transform() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_Transform(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C","H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_Transform_default() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_Transform(6);
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }
    @Test
    void namesFlux_Transform_switchIfEmpty() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_switchIfEmpty(6);
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concat();
        StepVerifier.create(namesFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }
    @Test
    void explore_concatWith() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concatWith();
        StepVerifier.create(namesFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }
    @Test
    void explore_concatWithMono() {
        var namesFlux = fluxAndMonoGeneratorService.explore_concatWith_Mono();
        StepVerifier.create(namesFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var namesFlux = fluxAndMonoGeneratorService.explore_merge();
        StepVerifier.create(namesFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }
    @Test
    void explore_mergeWith() {
        var namesFlux = fluxAndMonoGeneratorService.explore_mergeWith();
        StepVerifier.create(namesFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }
    @Test
    void explore_mergeWithMono() {
        var namesFlux = fluxAndMonoGeneratorService.explore_mergeWithMono();
        StepVerifier.create(namesFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        var namesFlux = fluxAndMonoGeneratorService.explore_mergeSequential();
        StepVerifier.create(namesFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_zip_withTwoFlux() {
        var namesFlux = fluxAndMonoGeneratorService.explore_zip_withTwoFlux();
        StepVerifier.create(namesFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }
    @Test
    void explore_zip_withMultipleFlux() {
        var namesFlux = fluxAndMonoGeneratorService.explore_zip_withMultipleFlux();
        StepVerifier.create(namesFlux)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }
}