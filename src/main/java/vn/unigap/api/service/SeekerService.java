package vn.unigap.api.service;

import java.util.List;
import vn.unigap.api.dto.in.SeekerRequestDto;
import vn.unigap.api.dto.out.SeekerResponseDto;

public interface SeekerService {
  void create(SeekerRequestDto seekerRequestDto);

  void update(Integer seekerId, SeekerRequestDto seekerRequestDto);

  SeekerResponseDto get(Integer seekerId);

  List<SeekerResponseDto> getAll(Integer provinceId, Integer page, Integer pageSize);

  void delete(Integer seekerId);
}
