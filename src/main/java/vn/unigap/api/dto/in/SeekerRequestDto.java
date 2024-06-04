package vn.unigap.api.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SeekerRequestDto {
  @NotBlank
  private String name;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate birthday;

  private String address;

  private Integer provinceId;
}
