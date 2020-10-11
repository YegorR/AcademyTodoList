package ru.yegorr.todolist.controller.errorhandling;

import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yegorr.todolist.dto.response.ExceptionResponse;
import ru.yegorr.todolist.exception.NotFoundException;

/**
 * Handler of application exceptions
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse(String.format("%s is not supported method", ex.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse(String.format("%s is not supported media type", ex.getContentType()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse(String.format("%s path variable is needed", ex.getVariableName()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse(
                String.format("%s(%s) parameter is missing", ex.getParameterName(), ex.getParameterType()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse("Server can not read the http message", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        return generateDefaultExceptionResponse(String.format("%s(%s) parameter validation fails", ex.getParameter().getParameterName(),
                ex.getParameter().getParameterType().getSimpleName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        return generateDefaultExceptionResponse(String.format("%s is not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<Object> handleOther() {
        return generateDefaultExceptionResponse("Some server error has happened", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<Object> generateDefaultExceptionResponse(String message, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(message);
        exceptionResponse.setHttpCode(httpStatus.value());
        return ResponseEntity.status(exceptionResponse.getHttpCode()).body(exceptionResponse);
    }
}
