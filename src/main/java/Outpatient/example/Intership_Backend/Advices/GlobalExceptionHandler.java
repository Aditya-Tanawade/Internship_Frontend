package Outpatient.example.Intership_Backend.Advices;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input Validation Failed")
                .subErrors(errorMessage)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "A database integrity violation occurred.";

        if (ex.getMessage().contains("email") && ex.getMessage().contains("already exists")) {
            errorMessage = "Email Already Exists! Please DO Login.";
        }

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }





    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .subErrors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }



}
