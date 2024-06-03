package vn.unigap.api.service;

import java.util.List;
import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.JobResponseDto;

public interface JobService {

  void create(JobCreateRequestDto jobCreateRequestDto);

  void update( Integer jobId ,JobUpdateRequestDto jobUpdateRequestDto);

  JobResponseDto get(Integer jobId);

  List<JobResponseDto> getAll(Integer employerId, Integer page, Integer pageSize);

  void delete(Integer jobId);
}
