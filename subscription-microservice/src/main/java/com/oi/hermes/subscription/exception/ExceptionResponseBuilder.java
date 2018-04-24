package com.oi.hermes.subscription.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.oi.hermes.subscription.exception.model.APIExceptionResponse;
import com.oi.hermes.subscription.exception.model.Links;

import brave.Tracer;

@Component
public class ExceptionResponseBuilder {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseBuilder.class);
	@Autowired
	APIExceptionResponse apiError;
	@Autowired
	Tracer trace;

	public APIExceptionResponse buildExceptionResponse(Exception ex, HttpServletRequest req) {
		List<String> validationErrors = new ArrayList<String>();
		apiError = new APIExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
		apiError.setMessage("Invalid authentication token");
		apiError.setStatus(HttpStatus.UNAUTHORIZED);
		if (trace != null && trace.currentSpan() != null) {
			apiError.setTraceId(trace.currentSpan().context().traceIdString());
		}
		apiError.setErrorCode("HERBUS401");
		apiError.set_links(buildLinks("HERBUS401", req));
		apiError.setDebugMessage(ex.getMessage());
		apiError.setSubErrors(validationErrors);
		return apiError;
	}

	protected Links buildLinks(String errorCode, HttpServletRequest req) {
		if (req == null) {
			final UriComponents uri = MvcUriComponentsBuilder.fromController(getClass()).path("/errors/")
					.path(errorCode).build();
			Links links = new Links(uri.toUriString());
			return links;
		} else {
			HttpServletRequest request = (HttpServletRequest) req;
			LOGGER.info("*********" + request.getRequestURL());

			Links links = new Links("");
			return links;
		}
	}

	public APIExceptionResponse buildExceptionResponse(Exception ex, String errorCode, String errorMessage,
			String traceIdString, HttpStatus status) {
		LOGGER.info("*****************validation error" + ex);
		List<String> validationErrors = new ArrayList<String>();
		apiError = new APIExceptionResponse(status, ex.getMessage());
		apiError.setMessage(errorMessage);
		apiError.setStatus(status);
		apiError.setTraceId(trace.currentSpan().context().traceIdString());
		apiError.setErrorCode(errorCode);
		apiError.set_links(buildLinks(errorCode, null));
		apiError.setDebugMessage(ex.getMessage());

		if (ex instanceof MethodArgumentNotValidException) {
			List<FieldError> fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
			List<ObjectError> globalErrors = ((MethodArgumentNotValidException) ex).getBindingResult()
					.getGlobalErrors();
			List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
			String error;
			for (FieldError fieldError : fieldErrors) {
				error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
				errors.add(error);
			}
			for (ObjectError objectError : globalErrors) {
				error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
				errors.add(error);
			}

			apiError.setSubErrors(errors);
		}
		if (ex instanceof RepositoryConstraintViolationException) {
			Errors err = ((RepositoryConstraintViolationException) ex).getErrors();
			List<ObjectError> globalErrors = err.getAllErrors();
			List<String> errors = new ArrayList<String>();
			String error;
			for (ObjectError objectError : globalErrors) {
				error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
				errors.add(error);
			}

			apiError.setSubErrors(errors);
		} else {
			apiError.setSubErrors(validationErrors);
		}
		return apiError;

	}
}
