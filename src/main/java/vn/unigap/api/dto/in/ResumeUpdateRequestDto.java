package vn.unigap.api.dto.in;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ResumeUpdateRequestDto {
  @NotNull
  private String careerObj;

  @NotNull
  private String title;

  @NotNull
  private BigDecimal salary;

  @NotNull
  private List<Integer> fieldIds;

  @NotNull
  private  List<Integer> provinceIds;
}
