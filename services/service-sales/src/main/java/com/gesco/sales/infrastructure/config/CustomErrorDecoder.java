package com.gesco.sales.infrastructure.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        
        if (status.is4xxClientError()) {
            return new ResponseStatusException(status, "Client error during inter-service call: " + methodKey);
        }
        
        if (status.is5xxServerError()) {
            return new ResponseStatusException(status, "Remote service failure during: " + methodKey);
        }
        
        return new Default().decode(methodKey, response);
    }
}
