package myrmod.jobapp.ExceptionHandler;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	// Handle ResourceNotFoundException for reactive controllers
	@ExceptionHandler(ResourceNotFoundException.class)
	public Mono<ResponseEntity<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
	}

	// Handle generic exceptions for reactive controllers
	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<Object>> handleGenericException(Exception ex) {
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()));
	}
}
