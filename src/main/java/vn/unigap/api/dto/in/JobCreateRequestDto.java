package vn.unigap.api.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class JobCreateRequestDto {

  @NotBlank
  private String title;

  @NotNull
  private Integer employerId;
  @NotNull
  private Integer quantity;

  @NotBlank
  private String description;

  @NotNull
  private List<Integer> fieldIds;

  @NotNull
  private  List<Integer> provinceIds;

  @NotNull
  private BigDecimal salary;

  @NotNull
  private LocalDateTime expiredAt;
}
