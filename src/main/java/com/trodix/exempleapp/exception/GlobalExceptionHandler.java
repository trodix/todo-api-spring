package com.trodix.exempleapp.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    // ===============================
    // = Handle specific exceptions
    // ===============================

    /** 
     * For creating a custom payload exception response :
    */

    // @ExceptionHandler(BadRequestException.class)
    // public ResponseEntity<?> handleBadRequestException(BadRequestException exception, WebRequest request) {
    //     ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        
    //     return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    // }



    /** 
     * For creating a standard payload for a custom exception :
    */

    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletResponse response) 
        throws IOException {
        
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public void handleBadRequestException(BadRequestException exception, HttpServletResponse response) 
        throws IOException {
        
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException exception, HttpServletResponse response) 
        throws IOException {
        
        response.sendError(HttpStatus.FORBIDDEN.value(), exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(BadCredentialsException exception, HttpServletResponse response) 
        throws IOException {
        
        response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
    }


    /**
     * Handle invalid path variable. ex: String to UUID conversion exception
     * @param exception
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, HttpServletResponse response) 
        throws IOException {
        
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }



    // ===============================
    // = Handle global exceptions
    // ===============================


    @ExceptionHandler(Exception.class)
    public void handleGlobalException(Exception exception, HttpServletResponse response)
        throws IOException {

        // response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal server error occured");
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}