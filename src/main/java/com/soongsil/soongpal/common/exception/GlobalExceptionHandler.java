package com.soongsil.soongpal.common.exception;


import com.soongsil.soongpal.common.dto.CommonErrorDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> IllegalArgumentExceptionHandler (IllegalArgumentException e) {
        log.error("[exceptionHandle] IllegalArgumentException", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<CommonErrorDto> handleChatException(ChatException e) {
        log.error("[exceptionHandle] ChatException", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> EntityNotFoundExceptionHandler (EntityNotFoundException e) {
        log.error("[exceptionHandle] EntityNotFoundException", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<CommonErrorDto> SecurityExceptionHandler (SecurityException e) {
        log.error("[exceptionHandle] SecurityException", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonErrorDto> runtimeExceptionHandler (RuntimeException e) {
        log.error("[exceptionHandle] RuntimeException", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorDto> exceptionHandler (Exception e) {
        log.error("[exceptionHandle] Exception", e);
        return new ResponseEntity<>(new CommonErrorDto(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
