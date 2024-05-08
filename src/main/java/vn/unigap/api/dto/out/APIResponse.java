package vn.unigap.api.dto.out;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponse<T> {
  private Integer errorCode;
  private Integer statusCode;
  private String message;
  private T object;
}
