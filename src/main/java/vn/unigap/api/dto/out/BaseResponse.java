package vn.unigap.api.dto.out;

import java.util.List;
import lombok.Data;

@Data
public class BaseResponse<T> {
  private Integer page;
  private Integer pageSize;
  private Long totalElements;
  private Long totalPages;
  private List<T> data;
}
