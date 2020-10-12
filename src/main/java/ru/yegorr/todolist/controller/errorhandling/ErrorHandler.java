package ru.yegorr.todolist.controller.errorhandling;

import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yegorr.todolist.dto.response.ExceptionResponse;
import ru.yegorr.todolist.exception.NotFoundException;

/**
 * Handler of application exceptions
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    private static ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return generateDefaultExceptionResponse(String.format("%s is not supported method", ex.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    private static ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return generateDefaultExceptionResponse(String.format("%s is not supported media type", ex.getContentType()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    private static ResponseEntity<Object> handleHttpMediaTypeNotAcceptable() {
        return generateDefaultExceptionResponse("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MissingPathVariableException.class})
    private static ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex) {
        return generateDefaultExceptionResponse(String.format("%s path variable is needed", ex.getVariableName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    private static ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return generateDefaultExceptionResponse(
                String.format("%s(%s) parameter is missing", ex.getParameterName(), ex.getParameterType()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    private static ResponseEntity<Object> handleHttpMessageNotReadable() {
        return generateDefaultExceptionResponse("Server can not read the http message", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private static ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return generateDefaultExceptionResponse(String.format("%s(%s) parameter validation fails", ex.getParameter().getParameterName(),
                ex.getParameter().getParameterType().getSimpleName()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    private static ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        return generateDefaultExceptionResponse(String.format("%s is not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Throwable.class})
    private static ResponseEntity<Object> handleOther() {
        return generateDefaultExceptionResponse("Some server error has happened", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    private static ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        return generateDefaultExceptionResponse("Argument type are wrong", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    private static ResponseEntity<Object> handleNoHandlerFound() {
        return generateDefaultExceptionResponse("The page is not found", HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<Object> generateDefaultExceptionResponse(String message, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(message);
        exceptionResponse.setHttpCode(httpStatus.value());
        return ResponseEntity.status(exceptionResponse.getHttpCode()).body(exceptionResponse);
    }
}