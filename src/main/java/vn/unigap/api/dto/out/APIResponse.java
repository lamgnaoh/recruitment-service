package vn.unigap.api.dto.out;


import lombok.Data;

@Data
public class APIResponse<T> {
  private Integer errorCode;
  private Integer statusCode;
  private String message;
  private BaseResponse<T> object;
}
