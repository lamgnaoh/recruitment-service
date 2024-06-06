package vn.unigap.api.service;

import java.util.List;
import vn.unigap.api.dto.in.ResumeCreateRequestDto;
import vn.unigap.api.dto.in.ResumeUpdateRequestDto;
import vn.unigap.api.dto.out.ResumeResponseDto;

public interface ResumeService {

  void create(ResumeCreateRequestDto resumeCreateRequestDto);

  void update(Integer resumeId,ResumeUpdateRequestDto resumeUpdateRequestDto);

  ResumeResponseDto get(Integer resumeId);

  List<ResumeResponseDto> getAll(Integer seekerId, Integer page, Integer pageSize);

  void delete(Integer resumeId);
}
