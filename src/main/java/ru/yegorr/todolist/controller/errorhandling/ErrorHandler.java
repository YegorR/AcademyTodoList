package ru.yegorr.todolist.controller.errorhandling;

import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.validation.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.yegorr.todolist.dto.response.ExceptionResponse;
import ru.yegorr.todolist.exception.*;

import javax.validation.*;

/**
 * Обработчик исключений приложения
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
        return generateDefaultExceptionResponse("Server can not read the http message, maybe wrong type?", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private static ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        StringBuilder errorString = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorString.append(String.format(" %s.", error.getDefaultMessage()));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorString.append(String.format(" %s.", error.getDefaultMessage()));
        }

        return generateDefaultExceptionResponse(String.format("Validation fails.%s", errorString.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    private static ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorString = new StringBuilder();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            errorString.append(String.format(" %s.", error.getMessage()));
        }

        return generateDefaultExceptionResponse(String.format("Validation fails.%s", errorString.toString()), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler({ValidationFailsException.class})
    private static ResponseEntity<Object> handleValidationFails(ValidationFailsException ex) {
        return generateDefaultExceptionResponse(String.format("Validation fails. %s", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UniqueCheckFallsException.class})
    private static ResponseEntity<Object> handleUniqueFalls(UniqueCheckFallsException exception) {
        return generateDefaultExceptionResponse(String.format("Field %s is not unique", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<Object> generateDefaultExceptionResponse(String message, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(message);
        exceptionResponse.setHttpCode(httpStatus.value());
        return ResponseEntity.status(exceptionResponse.getHttpCode()).body(exceptionResponse);
    }
}
