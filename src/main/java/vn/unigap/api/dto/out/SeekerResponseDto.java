package vn.unigap.api.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SeekerResponseDto {
  private Integer id;
  private String name;
  private LocalDate birthday;
  private String address;
  private Integer provinceId;
  private String provinceName;
}
