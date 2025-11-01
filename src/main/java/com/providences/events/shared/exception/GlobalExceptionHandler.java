package com.providences.events.shared.exception;

import com.providences.events.shared.dto.ApiResponse;
import com.providences.events.shared.dto.CustomErrorDTO;
import com.providences.events.shared.dto.ValidationErrorDTO;
import com.providences.events.shared.exception.exceptions.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> resourceNotFound(ResourceNotFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationErrorDTO>> methodArgumentNotValidation(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationErrorDTO err = new ValidationErrorDTO(
                Instant.now(),
                status.value(),
                "Dados inválidos",
                request.getRequestURI());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> forbidden(ForbiddenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> unauthorized(UsernameNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> httpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Formulario precisa ser preenchido",
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> error(AuthorizationDeniedException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Acesso negado!",
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> error(InvalidDataAccessApiUsageException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> notFound(NoResourceFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Nenhum endpoint encontrado",
                request.getRequestURI());
        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = "Violação de integridade dos dados.";

        if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
            message = "Registro duplicado: um valor único já existe no sistema.";
        } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign")) {
            message = "Não é possível excluir ou alterar este registro, pois há dados relacionados.";
        } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("Value too long for column")) {
            message = "Valor muito longo para um campo do banco de dados.";
        }

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                message,
                request.getRequestURI());

        e.printStackTrace();
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<CustomErrorDTO>> globalExceptionHandler(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Loga o erro no servidor (sem expor detalhes ao cliente)
        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                request.getRequestURI());

        return ResponseEntity.status(status).body(new ApiResponse<>(false, err));
    }
}
