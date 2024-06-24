package vn.unigap.api.dto.out;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceDto {
  private Integer id;
  private String name;
}
