package vn.unigap.api.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class ResumeResponseDto {
  private Integer id;
  private Integer seekerId;
  private String seekerName;
  private String careerObj;
  private String title;
  private BigDecimal salary;
  private List<FieldDto> fields;
  private List<ProvinceDto> provinces;
}
