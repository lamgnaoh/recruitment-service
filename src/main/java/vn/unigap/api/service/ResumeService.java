package vn.unigap.api.service;

import vn.unigap.api.dto.in.ResumeCreateRequestDto;
import vn.unigap.api.dto.in.ResumeUpdateRequestDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.dto.out.ResumeResponseDto;

public interface ResumeService {

  void create(ResumeCreateRequestDto resumeCreateRequestDto);

  void update(Integer resumeId,ResumeUpdateRequestDto resumeUpdateRequestDto);

  ResumeResponseDto get(Integer resumeId);

  PageResponse<ResumeResponseDto> getAll(Integer seekerId, Integer page, Integer pageSize);

  void delete(Integer resumeId);
}
