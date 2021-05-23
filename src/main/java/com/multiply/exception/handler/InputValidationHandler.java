package com.multiply.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.multiply.dto.InputFailedValidationResponse;
import com.multiply.exception.InputValidationException;

@ControllerAdvice
public class InputValidationHandler {

	 @ExceptionHandler(InputValidationException.class)
	public ResponseEntity<InputFailedValidationResponse> handleInvalidInput(InputValidationException ex) {
		 
		  new InputFailedValidationResponse();
		InputFailedValidationResponse response = InputFailedValidationResponse
				  .builder()
				  .errorCode(ex.getErrorCode())
		          .input(ex.getInput())
		          .message(ex.getMessage())
		          .build();

	        return ResponseEntity
	        		.status(HttpStatus.BAD_REQUEST)
	        		.body(response);
		
	}
}
