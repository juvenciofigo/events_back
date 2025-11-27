package com.providences.events.shared.exception;

import com.providences.events.shared.dto.CustomErrorDTO;
import com.providences.events.shared.dto.ValidationErrorDTO;
import com.providences.events.shared.exception.exceptions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> resourceNotFound(ResourceNotFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CustomErrorDTO> error(BusinessException e, HttpServletRequest request) {
        HttpStatus status = e.getStatusCode();
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CustomErrorDTO> error(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                404,
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(404).body(err);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomErrorDTO> error(NullPointerException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = e.getMessage();

        if (e.getMessage().contains("core.Authentication.getPrincipal")) {
            message = "Sem acesso";
            status = HttpStatus.FORBIDDEN;
        }

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorDTO> error(ConstraintViolationException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        List<String> messages = new ArrayList<>();

        messages = Arrays.asList(e.getMessage().split(":"));

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                messages.get(1),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> methodArgumentNotValidation(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationErrorDTO err = new ValidationErrorDTO(
                Instant.now(),
                status.value(),
                "",
                request.getRequestURI());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomErrorDTO> forbidden(ForbiddenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> unauthorized(UsernameNotFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorDTO> httpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Formulario precisa ser preenchido",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CustomErrorDTO> error(AuthorizationDeniedException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Acesso negado!",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<CustomErrorDTO> error(InvalidDataAccessApiUsageException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MpesaPaymentException.class)
    public ResponseEntity<CustomErrorDTO> mpesa(MpesaPaymentException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        String message = "";

        switch (e.getStatusCode()) {
            case 422:
                message = "Saldo insuficiente";
                break;
            case 408:
                message = "Tempo limite da requisição excedido Mpesa";
                status = HttpStatus.REQUEST_TIMEOUT;
                break;
            case 500:
                message = "Serviço temporariamente indisponível. Tente novamente em alguns minutos.";
                status = HttpStatus.REQUEST_TIMEOUT;
                break;

            default:
                message = "Falha no pagamento via M-Pesa.";
                break;
        }

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomErrorDTO> notFound(NoResourceFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Nenhum endpoint encontrado",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = "Violação de integridade dos dados.";

        if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
            message = "Registo duplicado: um valor único já existe no sistema.";
        } else if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign")) {
            message = "Não é possível excluir ou alterar este registo, pois há dados relacionados.";
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("O arquivo excede o tamanho máximo permitido!");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDTO> globalExceptionHandler(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Log o erro no servidor (sem expor detalhes ao cliente)
        System.err.println("Erro inesperado: " + e);
        e.printStackTrace();

        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(),
                status.value(),
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
}
