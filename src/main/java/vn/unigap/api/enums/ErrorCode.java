package vn.unigap.api.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  EMAIL_ALREADY_EXIST( 1, HttpStatus.BAD_REQUEST, "Email already exist"),
  PROVINCE_NOT_FOUND(2, HttpStatus.NOT_FOUND, "Province not found" ),
  EMPLOYER_NOT_FOUND(3,HttpStatus.NOT_FOUND ,  "Employer not found");

  private final Integer code;
  private final HttpStatus status;
  private final String message;

  ErrorCode( Integer errorCode ,HttpStatus status, String message) {
    this.code = errorCode;
    this.status = status;
    this.message = message;
  }

}
