package vn.unigap.api.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class JobResponseDto {
  private Integer id;
  private String title;
  private Integer quantity;
  private String description;
  private List<FieldDto> fields;
  private List<ProvinceDto> provinces;
  private BigDecimal salary;
  private LocalDateTime expiredAt;
  private Integer employerId;
  private String employerName;
}
