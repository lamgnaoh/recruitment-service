package vn.unigap.api.dto.out;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {
  private Integer page;
  private Integer pageSize;
  private Long totalElements;
  private Long totalPages;
  private List<T> data;
}
