package com.multiply.service;

import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.multiply.dto.MultiplyRequest;
import com.multiply.dto.MultiplyResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class MultiplyService {
	
	public Mono<MultiplyResponse> getSquare(int input){
		
System.out.println("num in service: "+input);
		return Mono.fromSupplier(()->{ //will execute only if someone subscribed to it
			return  MultiplyResponse
					.builder() //builder is a static method that present when we do @Builder
					.date(new Date())
					.output(input*input).build();
		}).publishOn(Schedulers.boundedElastic())
		  .retry(1)
		  .onErrorContinue((err,errObj)->{
			  System.out.println("Error by:"+errObj+" message: "+err.getMessage());
			  
		  })
		  .subscribeOn(Schedulers.boundedElastic());
	}
	
	public Flux<MultiplyResponse> getMultiplyTable(int input){
		

		Consumer<FluxSink<Integer>> fluskSinkConsumer=(fluskSink)->{
			for(int i=1;i<=10&&!fluskSink.isCancelled();i++) { //fluskSink.isCancelled doesn't push if the flux is cancelled or page is refreshed
				fluskSink.next(i);
			}
			fluskSink.complete();
		};

		return Flux.create(fluskSinkConsumer)
				.publishOn(Schedulers.boundedElastic())
				.delayElements(Duration.ofSeconds(1)) // this is a non-blocking sleep
				.doOnNext((i)->System.out.println("doNext for index:"+i))
				 .onErrorContinue((err,errObj)->{
					  System.out.println("Error by:"+errObj+" message: "+err.getMessage());
					  
				  })
				.map((i)->{
					return  MultiplyResponse
							.builder()
							.date(new Date())
							.output(i*input).build();
				})
				.switchIfEmpty((i)->fallBacknmultiply())
				.retry(1)
				.onBackpressureDrop()
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	public Mono<Integer> fallBacknmultiply(){
		return Mono.just(-1);
	}

	public Mono<MultiplyResponse> getMultiplication(Mono<MultiplyRequest> multiplyRequest){
		return multiplyRequest
				.map((mulObj)->MultiplyResponse
						.builder()
						.date(new Date())
						.output(mulObj.getFirst()*mulObj.getSecond())
						.build()
						)
				.retry(1)
				.subscribeOn(Schedulers.boundedElastic());
	}
}
