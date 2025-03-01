package io.gittul.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        log.warn("CustomException occurred: status={}, message={}", ex.getStatus(), ex.getMessage(), ex.getCause());

        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatus().value());
        response.put("message", ex.getMessage());

        if (ex.getCause() != null) {
            response.put("cause", ex.getCause().getMessage());
        }

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "서버에서 예상치 못한 오류가 발생했습니다. 관리자에게 문의하세요.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
