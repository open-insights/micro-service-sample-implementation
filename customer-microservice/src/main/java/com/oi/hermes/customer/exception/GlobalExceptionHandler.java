package com.oi.hermes.customer.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.oi.hermes.customer.exception.model.APIExceptionResponse;
import com.oi.hermes.customer.exception.type.EntityAlreadyPresentException;
import com.oi.hermes.customer.exception.type.GeneralException;
import com.oi.hermes.customer.exception.type.InvalidCredentialsException;
import com.oi.hermes.customer.exception.type.ResourceNotAcceptableException;
import com.oi.hermes.customer.exception.type.ResourceNotModifiedException;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.transaction.TransactionSystemException;
import brave.Tracer;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = { "com.oi.hermes.customer.dao", "com.oi.hermes.customer.controller",
		"com.oi.hermes.customer.security.controller" }, basePackageClasses = RepositoryRestExceptionHandler.class)
@PropertySource(value = { "classpath:BusinessExceptionCodes.properties" })
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	public static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	APIExceptionResponse apiError;
	@Autowired
	ExceptionResponseBuilder exceptionResponseBuilder;
	@Autowired
	Tracer trace;
	@Autowired
	Environment env;

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RepositoryConstraintViolationException.class)
	public ResponseEntity<Object> handleAccessDeniedException(RepositoryConstraintViolationException ex,
			WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ javax.validation.ConstraintViolationException.class,
			org.hibernate.exception.ConstraintViolationException.class })
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {

		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionSystemException.class)
	protected ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex) throws Exception {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.NOT_FOUND);

	}

	/************ Custom Exceptions ***********************/

	@ExceptionHandler(InvalidCredentialsException.class)
	protected ResponseEntity<Object> handleAuthenticationException(InvalidCredentialsException ex) throws Exception {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.UNAUTHORIZED.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(ResourceNotAcceptableException.class)
	protected ResponseEntity<Object> handleResourceNotAcceptableException(ResourceNotAcceptableException ex) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(ResourceNotModifiedException.class)
	protected ResponseEntity<Object> handleResourceNotModifiedException(ResourceNotModifiedException ex) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.NOT_MODIFIED.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.NOT_MODIFIED);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.NOT_MODIFIED);
	}

	@ExceptionHandler(EntityAlreadyPresentException.class)
	protected ResponseEntity<?> handleBusinessException(EntityAlreadyPresentException ex) throws Exception {

		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.CONFLICT);
		return new ResponseEntity<APIExceptionResponse>(apiExceptionResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(GeneralException.class)
	protected ResponseEntity<Object> handleGeneralException(GeneralException ex) {
		APIExceptionResponse apiExceptionResponse = exceptionResponseBuilder.buildExceptionResponse(ex,
				String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage(),
				trace.currentSpan().context().traceIdString(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
