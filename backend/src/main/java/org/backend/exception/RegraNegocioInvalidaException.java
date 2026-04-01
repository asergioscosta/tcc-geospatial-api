package org.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegraNegocioInvalidaException extends RuntimeException {
    public RegraNegocioInvalidaException(String message) {
        super(message);
    }
}