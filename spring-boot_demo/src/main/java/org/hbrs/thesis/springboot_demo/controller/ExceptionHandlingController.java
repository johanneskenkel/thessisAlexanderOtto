
package org.hbrs.thesis.springboot_demo.controller;

import org.hbrs.thesis.springboot_demo.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {
    private static final Logger exceptionHandlerLogger = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleExceptions(
        Exception ex, WebRequest request) {
        MessageDto messageDto = new MessageDto("Something went wrong. " + ex.getMessage());
        exceptionHandlerLogger.warn("There was an exception: {}, that was thrown with the message: {}",
                ex.getClass().getSimpleName(), ex.getLocalizedMessage());
        return handleExceptionInternal(ex, messageDto,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
