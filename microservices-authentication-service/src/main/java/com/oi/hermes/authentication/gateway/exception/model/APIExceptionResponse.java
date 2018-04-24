package com.oi.hermes.authentication.gateway.exception.model;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Component
public class APIExceptionResponse {

	private HttpStatus status;
	private String errorCode;
	private String traceId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private Links _links;
	private List<String> subErrors;

	private APIExceptionResponse() {
		timestamp = LocalDateTime.now();
	}

	public APIExceptionResponse(HttpStatus status, String message) {
		this();
		this.status = status;
		this.message = message;
	}

	public APIExceptionResponse(HttpStatus status, String message, String errorCode) {
		this();
		this.status = status;
		this.message = message;
		this.errorCode = errorCode;
	}

	APIExceptionResponse(HttpStatus status, String errorCode, List<String> subErrors) {
		this();
		this.status = status;
		this.subErrors = subErrors;
		this.errorCode = errorCode;
	}

	public APIExceptionResponse(HttpStatus status, Throwable ex, String errorCode, List<String> subErrors) {
		this();
		this.status = status;
		this.errorCode = errorCode;
		this.message = ex.getMessage();
		this.debugMessage = ex.getLocalizedMessage();
		this.subErrors = subErrors;
	}

	APIExceptionResponse(String object, String message) {
		this();
		this.message = message;
	}

	public APIExceptionResponse(HttpStatus status, String errorCode, String traceId, LocalDateTime timestamp,
			String message, String debugMessage, Links _links, List<String> subErrors) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.traceId = traceId;
		this.timestamp = timestamp;
		this.message = message;
		this.debugMessage = debugMessage;
		this._links = _links;
		this.subErrors = subErrors;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public Links get_links() {
		return _links;
	}

	public void set_links(Links _links) {
		this._links = _links;
	}

	public List<String> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<String> subErrors) {
		this.subErrors = subErrors;
	}

}
