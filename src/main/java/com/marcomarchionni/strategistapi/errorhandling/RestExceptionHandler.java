package com.marcomarchionni.strategistapi.errorhandling;

import com.marcomarchionni.strategistapi.errorhandling.exceptions.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomExceptions(ErrorResponse ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getBody());
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage());
        pd.setType(URI.create("max-upload-size-exceeded"));
        pd.setTitle("Max upload size exceeded");
        if (ex.getCause() instanceof IllegalStateException illegalStateException) {
            pd.setDetail(illegalStateException.getMessage());
        }
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException constraintViolationEx) {
            String constraintName = constraintViolationEx.getConstraintName();
            ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, constraintName);
            pd.setType(URI.create("data-integrity-violation"));
            pd.setTitle("Data integrity violation");
            return ResponseEntity.status(pd.getStatus()).body(pd);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getRootCause());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        pd.setType(URI.create("access-denied"));
        pd.setTitle("Access denied");
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleUnauthorized(AuthenticationException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setType(URI.create("unauthorized"));
        pd.setTitle("Unauthorized");
        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                "The token has expired. " + ex.getMessage());
        pd.setType(URI.create("token-expired"));
        pd.setTitle("Token Expired");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(pd);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        // Combine all the constraint violations into a single string
        String detail = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setType(URI.create("invalid-parameter"));
        pd.setTitle("Invalid parameter(s)");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("The parameter '%s' has an invalid value '%s'. Expected values: %s",
                ex.getName(), ex.getValue(), Arrays.toString(Objects.requireNonNull(ex.getRequiredType())
                        .getEnumConstants()));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setType(URI.create("parameter-type-mismatch"));
        pd.setTitle("Parameter Type Mismatch");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler({ServletException.class, IOException.class})
    public final ResponseEntity<Object> handleServerExceptions(Exception ex) {
        String detail = "An internal server error occurred. " + ex.getMessage();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detail);
        pd.setType(URI.create("internal-server-error"));
        pd.setTitle("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }


    @Override
    @Nullable
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("endpoint-not-found"));
        pd.setTitle("Endpoint not found");
        return handleExceptionInternal(ex, pd, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = ex.getBody();
        body.setType(URI.create("invalid-parameter"));
        body.setTitle("Invalid parameter(s)");
        String detail = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" - "));
        if (!detail.isEmpty()) {
            body.setDetail(detail);
        }
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail body = createProblemDetail(ex, status, "Failed to read request. Request body is null or " +
                "invalid", null, null, request);
        body.setTitle("Request body not readable");
        body.setType(URI.create("request-body-not-readable"));
        return handleExceptionInternal(ex, body, headers, status, request);
    }
}
