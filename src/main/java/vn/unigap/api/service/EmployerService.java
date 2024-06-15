package vn.unigap.api.service;


import vn.unigap.api.dto.in.EmployerCreateRequestDto;
import vn.unigap.api.dto.in.EmployerUpdateRequestDto;
import vn.unigap.api.dto.out.EmployerResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.entity.Employer;

public interface EmployerService {

  Employer createEmployer(EmployerCreateRequestDto employerCreateRequestDto);

  Employer update(Integer employerId , EmployerUpdateRequestDto employerCreateRequestDto);

  EmployerResponseDto get(Integer employerId);

  PageResponse<EmployerResponseDto> getAll(Integer page, Integer pageSize);

  Employer delete(Integer employerId);
}
