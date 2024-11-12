package com.example.demo;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class MonoFluxController {

	@GetMapping("/MonoType")
	public ResponseEntity<Mono<BindingClass>> getMonoType() {
		BindingClass bd = new BindingClass("pavan", new Date());
		Mono<BindingClass> just = Mono.just(bd);

		

		return new ResponseEntity<Mono<BindingClass>>(just, HttpStatus.OK);
	}

	@GetMapping(value="/fluxType",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<Flux<BindingClass>> getFluxType() {
		BindingClass bd = new BindingClass("pavan", new Date());
		Stream<BindingClass> stream = Stream.generate(() -> bd).limit(2);
	    
	    // Creating a Flux from the finite stream
	    Flux<BindingClass> fromStream = Flux.fromStream(stream);
	    
	    // Creating an interval Flux that emits every 5 seconds
	    Flux<Long> interval = Flux.interval(Duration.ofSeconds(5)).take(10);
	    
	    // Zipping the two Fluxes together
	    Flux<BindingClass> fluxMap = Flux.zip(fromStream, interval)
	                                     .map(Tuple2::getT1);

		
		return new ResponseEntity<Flux<BindingClass>>(fluxMap, HttpStatus.OK);
	}

}
