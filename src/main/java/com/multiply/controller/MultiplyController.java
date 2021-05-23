package com.multiply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.multiply.service.MultiplyService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.multiply.dto.MultiplyRequest;
import com.multiply.dto.MultiplyResponse;
import com.multiply.exception.InputValidationException;

@RestController
@RequestMapping("/api")
public class MultiplyController {
	
	@Autowired
	MultiplyService multiplyService;
	@Autowired
	WebClient webClient;
	
	@GetMapping(value="square/{num}")
	public Mono<MultiplyResponse> findSquare(@PathVariable(name = "num") int num){
		
		verifyInput(num);
		return multiplyService.getSquare(num);
	}
	
	@GetMapping(value="multitable/{num}")
	public Flux<MultiplyResponse> findMultiplicationTable(@PathVariable(name = "num") int num){
		verifyInput(num);
		System.out.println("server thread name from threadpool: "+Thread.currentThread().getName());
		return multiplyService.getMultiplyTable(num);
	}
	
	@GetMapping(value = "multitable/{num}/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<MultiplyResponse> findMultiplicationTableStream(@PathVariable(name = "num") int num){
		verifyInput(num);
		System.out.println("server thread name from threadpool: "+Thread.currentThread().getName());
		return multiplyService.getMultiplyTable(num);
	}
	
	
	@PostMapping(value="/multiply")
	public Mono<MultiplyResponse> getMultiplication
	(@RequestBody Mono<MultiplyRequest> multiplyRequest){
		return multiplyService.getMultiplication(multiplyRequest);
	}
	
	public void verifyInput(int num) {
		if(num>10) {
			throw new InputValidationException(num);
		}
	}

}
