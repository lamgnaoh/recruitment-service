package vn.unigap.api.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.unigap.api.enums.ErrorCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException{

  private final ErrorCode errorCode;

  public ApiException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}


