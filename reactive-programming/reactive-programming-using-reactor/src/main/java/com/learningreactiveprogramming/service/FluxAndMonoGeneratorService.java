package com.learningreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class FluxAndMonoGeneratorService {
    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
    }
    public Mono<String> nameMono(){
        return Mono.just("alex")
                .log();
    }

    public Flux<String> namesFlux_Map(){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > 3)
                .map(s->s.length() + "-" + s)
                .log();
    }
    public Flux<String> namesFlux_Immutability(){
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> namesFlux_FlatMap_async(int strLen){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > strLen)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_FlatMap(int strLen){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > strLen)
                .flatMap(this::splitString)
                .log();
    }
    public Flux<String> splitString(String s){
        return Flux.fromArray(s.split(""));
    }
    public Flux<String> splitString_withDelay(String s){
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(s.split(""))
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFlux_concatMap(int strLen){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s-> s.length() > strLen)
                .concatMap(this::splitString_withDelay)
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int strLen){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length()>strLen)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_flatMapMany(int strLen){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length()>strLen)
                .flatMapMany(this::splitString)
                .log();
    }
    public Mono<List<String>> splitStringMono(String s){
        var charArray = s.split("");
        return Mono.just(List.of(charArray));
    }

    public Flux<String> namesFlux_Transform(int strLen){
        Function<Flux<String>, Flux<String>> filterMap = names->names
                .map(String::toUpperCase)
                .filter(s->s.length()>strLen);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_switchIfEmpty(int strLen){
        Function<Flux<String>, Flux<String>> filterMap = names->names
                .map(String::toUpperCase)
                .filter(s->s.length()>strLen)
                .flatMap(this::splitString);
        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat(){
        var flux1 = Flux.just("A","B","C");
        var flux2 = Flux.just("D","E","F");
        return Flux.concat(flux1, flux2).log();
    }

    public Flux<String> explore_concatWith(){
        var flux1 = Flux.just("A","B","C");
        var flux2 = Flux.just("D","E","F");
        return flux1.concatWith(flux2).log();
    }
    public Flux<String> explore_concatWith_Mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge(){
        var flux1 = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(flux1, flux2).log();
    }


    public Flux<String> explore_mergeWith(){
        var flux1 = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));
        return flux1.mergeWith(flux2).log();
    }

    public Flux<String> explore_mergeWithMono(){
        var mono1 = Mono.just("A");
        var mono2 = Mono.just("B");
        return mono1.mergeWith(mono2).log();
    }

    public Flux<String> explore_mergeSequential(){
        var flux1 = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(flux1, flux2).log();
    }

    public Flux<String> explore_zip_withTwoFlux(){
        var flux1 = Flux.just("A","B","C");
        var flux2 = Flux.just("D","E","F");
        return Flux.zip(flux1, flux2,(s1, s2)->s1+s2).log();
    }

    public Flux<String> explore_zip_withMultipleFlux(){
        var flux1 = Flux.just("A","B","C");
        var flux2 = Flux.just("D","E","F");
        var flux3 = Flux.just("1","2","3");
        var flux4 = Flux.just("4","5","6");
        return Flux.zip(flux1, flux2, flux3, flux4)
                .map(tuple->tuple.getT1()+tuple.getT2()
                        +tuple.getT3()+tuple.getT4())
                .log();
    }

    public Flux<String> explore_zipWithTwoFlux(){
        var flux1 = Flux.just("A","B","C");
        var flux2 = Flux.just("D","E","F");
        return flux1.zipWith(flux2,(s1, s2)->s1+s2).log();
    }

    public Mono<String> explore_zip_withTwoMono(){
        var mono1 = Mono.just("A");
        var mono2 = Mono.just("D");
        return Mono.zip(mono1, mono2,(s1, s2)->s1+s2).log();
    }
    public Mono<String> explore_zip_withMultipleMono(){
        var mono1 = Mono.just("A");
        var mono2 = Mono.just("B");
        var mono3 = Mono.just("C");
        var mono4 = Mono.just("D");
        return Mono.zip(mono1, mono2,mono3,mono4)
                .map(t->t.getT1()+t.getT2()+t.getT3()+t.getT4())
                .log();
    }

    public static void main(String[] args) {
        Logger.getLogger("reactor").setLevel(Level.FINE);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.explore_zip_withMultipleMono()
                .subscribe(System.out::println);
/*        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is : " + name));
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> System.out.println("Mono name is : " + name));*/

    }
}
