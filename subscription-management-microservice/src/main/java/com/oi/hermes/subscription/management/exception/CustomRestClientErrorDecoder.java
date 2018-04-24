package com.oi.hermes.subscription.management.exception;

import org.springframework.stereotype.Component;
import com.oi.hermes.subscription.management.exception.type.CustomFeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class CustomRestClientErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.status() == 400) {
			throw new CustomFeignClientException(response.status(),
					response.reason() == null ? "Bad Request" : response.reason());
		} else if (response.status() == 500) {
			throw new CustomFeignClientException(response.status(),
					response.reason() == null ? "Internal sesrver Error" : response.reason());
		} else if (response.status() == 404) {
			throw new CustomFeignClientException(response.status(),
					response.reason() == null ? "Resource Not Found" : response.reason());
		} else if (response.status() == 409) {
			throw new CustomFeignClientException(response.status(),
					response.reason() == null ? "Resource already exists" : response.reason());
		}
		throw new CustomFeignClientException(500, "Something went wrong");
	}

}