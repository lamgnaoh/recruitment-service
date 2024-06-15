package vn.unigap.api.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import vn.unigap.api.dto.out.APIResponse;

@ControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
  public ResponseEntity<APIResponse<?>> handleAPIException(ApiException e) {
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(APIResponse.error(e.getErrorCode().getMessage(), e.getErrorCode().getStatus(),
            e.getErrorCode().getCode()));
  }

}
