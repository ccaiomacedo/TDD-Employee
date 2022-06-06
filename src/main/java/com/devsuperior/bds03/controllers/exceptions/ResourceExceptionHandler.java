package com.devsuperior.bds03.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

//essa classe serve pra interceptar a exceção que vai dar e esse controller que vai tratar
@ControllerAdvice
public class ResourceExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) { //httpServelet pq ele que tem as informações da minha requisição
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError();
        err.setTimeStamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Validation exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI()); //pega o caminho da requisição que eu fiz

        for(FieldError f : e.getBindingResult().getFieldErrors()){ //percorre a lista de erros e pega o campo do erro
            err.addError(f.getField(),f.getDefaultMessage()); // adiciona o campo e a mensagem na minha lista
        }

        return ResponseEntity.status(status).body(err);
    }
}
