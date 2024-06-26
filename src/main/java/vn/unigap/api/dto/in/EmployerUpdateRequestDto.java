package vn.unigap.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployerUpdateRequestDto {

  @NotBlank
  @Size(max = 255)
  private String name;
  @NotNull
  private Integer provinceId;

  private String description;
}
