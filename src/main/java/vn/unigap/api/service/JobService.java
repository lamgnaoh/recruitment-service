package vn.unigap.api.service;

import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.JobResponseDto;
import vn.unigap.api.dto.out.PageResponse;

public interface JobService {

  void create(JobCreateRequestDto jobCreateRequestDto);

  void update( Integer jobId ,JobUpdateRequestDto jobUpdateRequestDto);

  JobResponseDto get(Integer jobId);

  PageResponse<JobResponseDto> getAll(Integer employerId, Integer page, Integer pageSize);

  void delete(Integer jobId);

  JobResponseDto getJobDetailAndSeekerRelated(Integer jobId);
}
