package com.ThreadLine.backend.exception;

import com.ThreadLine.backend.exception.internal.InvalidSimulationConfigException;
import com.ThreadLine.backend.exception.internal.MachineOperationException;
import com.ThreadLine.backend.exception.internal.QueueOperationException;
import com.ThreadLine.backend.exception.wrapper.JsonProcessingExceptionWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSimulationConfigException.class)
    public ResponseEntity<?> handleInvalidSimulationConfigException(InvalidSimulationConfigException ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingExceptionWrapper.class)
    public ResponseEntity<?> handleJsonProcessingExceptionWrapper(JsonProcessingExceptionWrapper ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QueueOperationException.class)
    public ResponseEntity<?> handleQueueOperationException(QueueOperationException ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MachineOperationException.class)
    public ResponseEntity<?> handleMachineOperationException(MachineOperationException ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
