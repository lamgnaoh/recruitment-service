package vn.unigap.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.AppUtils;
import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.FieldDto;
import vn.unigap.api.dto.out.JobResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.dto.out.ProvinceDto;
import vn.unigap.api.entity.Employer;
import vn.unigap.api.entity.Job;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.JobFieldRepository;
import vn.unigap.api.repository.JobReposiroty;
import vn.unigap.api.repository.ProvinceRepository;
import vn.unigap.api.service.JobService;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

  private final JobReposiroty jobRepository;
  private final EmployerRepository employerRepository;
  private final ProvinceRepository provinceRepository;
  private final JobFieldRepository jobFieldRepository;
  private final AppUtils appUtils;

  @Override
  public void create(JobCreateRequestDto jobCreateRequestDto) {
    Integer employerId = jobCreateRequestDto.getEmployerId();
    Employer employer = employerRepository.findById(employerId)
        .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_ALREADY_EXIST));
    List<Integer> provinceIds = jobCreateRequestDto.getProvinceIds();
    provinceIds.stream().map(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND))).toList();
    List<Integer> fieldIds = jobCreateRequestDto.getFieldIds();
    fieldIds.stream().map(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND))).toList();
    Job job = Job.builder().title(jobCreateRequestDto.getTitle())
        .quantity(jobCreateRequestDto.getQuantity())
        .description(jobCreateRequestDto.getDescription()).salary(jobCreateRequestDto.getSalary())
        .employer(employer).provinces(
            "-" + provinceIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-")
        .fields("-" + fieldIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-")
        .salary(jobCreateRequestDto.getSalary()).expiredAt(jobCreateRequestDto.getExpiredAt())
        .build();
    jobRepository.save(job);
  }

  @Override
  public void update(Integer jobId, JobUpdateRequestDto jobUpdateRequestDto) {
    Job existJob = jobRepository.findById(jobId)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_NOT_FOUND));
    if (jobUpdateRequestDto.getTitle() != null) {
      existJob.setTitle(jobUpdateRequestDto.getTitle());
    }
    if (jobUpdateRequestDto.getQuantity() != null) {
      existJob.setQuantity(jobUpdateRequestDto.getQuantity());
    }
    if (jobUpdateRequestDto.getDescription() != null) {
      existJob.setDescription(jobUpdateRequestDto.getDescription());
    }
    List<Integer> fieldIds = jobUpdateRequestDto.getFieldIds();
    fieldIds.stream().map(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND))).toList();
    List<Integer> provinceIds = jobUpdateRequestDto.getProvinceIds();
    provinceIds.stream().map(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND))).toList();
    existJob.setProvinces(
        "-" + provinceIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-");
    existJob.setFields(
        "-" + fieldIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-");
    if (jobUpdateRequestDto.getSalary() != null) {
      existJob.setSalary(jobUpdateRequestDto.getSalary());
    }
    if (jobUpdateRequestDto.getExpiredAt() != null) {
      existJob.setExpiredAt(jobUpdateRequestDto.getExpiredAt());
    }
    jobRepository.save(existJob);
  }

  @Override
  public JobResponseDto get(Integer jobId) {
    Job existJob = jobRepository.findById(jobId)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_NOT_FOUND));
    List<FieldDto> fieldDtos = appUtils.getFieldDtoFromIds(
        existJob.getFields().substring(1, existJob.getFields().length() - 1).split("-"));

    List<ProvinceDto> provinceDtos = appUtils.getProvinceDtoFromIds(
        existJob.getProvinces().substring(1, existJob.getProvinces().length() - 1).split("-"));

    return JobResponseDto.builder().id(existJob.getId()).title(existJob.getTitle())
        .quantity(existJob.getQuantity()).description(existJob.getDescription()).fields(fieldDtos)
        .provinces(provinceDtos).salary(existJob.getSalary()).expiredAt(existJob.getExpiredAt())
        .employerId(existJob.getEmployer() != null ? existJob.getEmployer().getId() : null)
        .employerName(existJob.getEmployer() != null ? existJob.getEmployer().getName() : null)
        .build();
  }

  @Override
  public PageResponse<JobResponseDto> getAll(Integer employerId, Integer page, Integer pageSize) {
    Pageable paging = PageRequest.of(page - 1, pageSize);
    Page<Job> jobs;
    if (employerId == -1) {
      jobs = jobRepository.findAll(paging);
    } else {
      jobs = jobRepository.findAllByEmployerId(employerId, paging);
    }
    List<JobResponseDto> jobResponseDtos = jobs.getContent().stream().map(
            job -> JobResponseDto.builder().id(job.getId()).title(job.getTitle())
                .quantity(job.getQuantity()).salary(job.getSalary()).expiredAt(job.getExpiredAt())
                .employerId(job.getEmployer() != null ? job.getEmployer().getId() : null)
                .employerName(job.getEmployer() != null ? job.getEmployer().getName() : null).build())
        .toList();
    return PageResponse.<JobResponseDto>builder().page(page).pageSize(pageSize)
        .totalElements(jobs.getTotalElements()).totalPages((long) jobs.getTotalPages())
        .data(jobResponseDtos).build();
  }

  @Override
  public void delete(Integer jobId) {
    Job existJob = jobRepository.findById(jobId)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_NOT_FOUND));
    jobRepository.delete(existJob);
  }
}
