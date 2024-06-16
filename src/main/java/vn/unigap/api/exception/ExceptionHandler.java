package vn.unigap.api.exception;


import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.unigap.api.dto.out.APIResponse;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
  public ResponseEntity<APIResponse<?>> handleAPIException(ApiException e) {
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(APIResponse.error(e.getErrorCode().getMessage(), e.getErrorCode().getStatus(),
            e.getErrorCode().getCode()));
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    String supportedMethods =
        ex.getSupportedMethods() == null ? null : String.join(", ", ex.getSupportedMethods());
    String errorMessage = "Method not supported. Supported methods are " + supportedMethods;
    return ResponseEntity.status(status)
        .body(APIResponse.error(errorMessage, HttpStatus.METHOD_NOT_ALLOWED, status.value()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String fieldErrors = ex.getFieldErrors().stream()
        .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
        .collect(Collectors.joining(","));

    String errorMessage = String.format("MethodArgumentNotValid field errors: %s", fieldErrors);
    return new ResponseEntity<>(APIResponse.error(errorMessage, HttpStatus.BAD_REQUEST, 400),
        HttpStatus.BAD_REQUEST);
  }

}
