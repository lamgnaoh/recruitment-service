package vn.unigap.api.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class EmployerResponseDto {
  private Integer id;
  private String email;
  private String name;
  private Integer provinceId;
  private String provinceName;
  private String description;
}
