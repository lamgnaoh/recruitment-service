package vn.unigap.api.dto.out;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {
  private Integer errorCode;
  private Integer statusCode;
  private String message;
  private T object;

  public static <T> APIResponse <T> success(T object){
    return APIResponse.<T>builder()
        .statusCode(HttpStatus.OK.value())
        .object(object)
        .build();
  }

  public static <T> APIResponse <T> success(HttpStatus status,T object){
    return APIResponse.<T>builder()
        .statusCode(status.value())
        .object(object)
        .build();
  }


  public static <T> APIResponse <T> error(String message, HttpStatus status , Integer errorCode){
    return APIResponse.<T>builder()
        .errorCode(errorCode)
        .statusCode(status.value())
        .message(message)
        .build();
  }
}
