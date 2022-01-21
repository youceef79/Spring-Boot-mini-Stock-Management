package org.sid.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



public class ErrorHandling  extends ResponseEntityExceptionHandler{
	
	/******************  handling internal errors      
	 * @throws InterruptedException **************************/
	/*
	@Override
	protected ResponseEntity<Object>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,HttpStatus status, WebRequest request) {
	    Map<String, String> errors = new HashMap<>();
        
	    if ( ex.getBindingResult() != null) {
			  ex.getBindingResult().getFieldErrors().forEach(error -> 
		        errors.put(error.getField(), error.getDefaultMessage()));
		         return this.handleExceptionInternal(ex ,errors, headers, status, request);
		    } 
     
	    return this.handleExceptionInternal(ex ,errors, headers, status, request);
	}
	*/
	/*
	@Override
  	protected ResponseEntity<Object> handleBindException(
			BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
          
		  Map<String, String> errors = new HashMap<>();
		  //ResponseEntity<Object>
			 ex.getBindingResult().getFieldErrors().forEach(error -> 
		        errors.put(error.getField(), error.getDefaultMessage()));
		         
			  //errors.put("view", "FormEdit");
		           
		         //return (ResponseEntity<Object>) ResponseEntity.ok();
		  
			  //return errors;
			   return new ResponseEntity<>(errors ,headers, status);
			  }
		*/	  
	
	/******************  Rest of code      **************************/
	
	

}
