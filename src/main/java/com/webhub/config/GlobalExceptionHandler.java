package com.webhub.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(Exception.class) // here in this class, we have to give Exception or custom exception class name
	    public ResponseEntity<com.webhub.config.ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
	       com.webhub.config.ErrorResponse resp= new com.webhub.config.ErrorResponse( HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                ex.getMessage(),
	                System.currentTimeMillis());

	        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	 
	 @ExceptionHandler(ResourceNotFoundException.class)
	 public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
	        ErrorResponse errorResponse = new ErrorResponse(
	                HttpStatus.NOT_FOUND.value(),
	                ex.getMessage(),
	                System.currentTimeMillis()
	        );

	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }

}
