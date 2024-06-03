package vn.unigap.api.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.FieldDto;
import vn.unigap.api.dto.out.JobResponseDto;
import vn.unigap.api.dto.out.ProvinceDto;
import vn.unigap.api.entity.Employer;
import vn.unigap.api.entity.Job;
import vn.unigap.api.entity.JobField;
import vn.unigap.api.entity.Province;
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
    List<JobField> jobFields = jobFieldRepository.findByIdIn(Arrays.stream(
            existJob.getFields().substring(1, existJob.getFields().length() - 1).split("-"))
        .map(Integer::valueOf).toList());
    List<FieldDto> fieldDtos = jobFields.stream()
        .map(el -> FieldDto.builder().id(el.getId()).name(el.getField()).build()).toList();

    List<Province> provinces = provinceRepository.findByIdIn(
        Arrays.stream(existJob.getProvinces()
                .substring(1, existJob.getProvinces().length() - 1).split("-"))
        .map(Integer::valueOf).toList());
    List<ProvinceDto> provinceDtos = provinces.stream()
        .map(el -> ProvinceDto.builder().id(el.getId()).name(el.getName()).build()).toList();

    return JobResponseDto.builder()
        .id(existJob.getId()).title(existJob.getTitle())
        .quantity(existJob.getQuantity())
        .description(existJob.getDescription())
        .fields(fieldDtos)
        .provinces(provinceDtos)
        .salary(existJob.getSalary())
        .expiredAt(existJob.getExpiredAt())
        .employerId(existJob.getEmployer().getId())
        .employerName(existJob.getEmployer().getName()).build();
  }

  @Override
  public List<JobResponseDto> getAll(Integer employerId, Integer page, Integer pageSize) {
    Pageable paging = PageRequest.of(page - 1, pageSize);
    List<Job> jobs;
    if (employerId == -1) {
      jobs = jobRepository.findAll(paging).get().toList();
    } else {
      jobs = jobRepository.findAllByEmployerId(employerId, paging).get().toList();
    }
    return jobs.stream().map(job -> JobResponseDto.builder()
            .id(job.getId())
            .title(job.getTitle())
            .quantity(job.getQuantity())
            .salary(job.getSalary())
            .expiredAt(job.getExpiredAt())
            .employerId(job.getEmployer().getId())
            .employerName(job.getEmployer().getName())
            .build())
        .toList();
  }

  @Override
  public void delete(Integer jobId) {
    Job existJob = jobRepository.findById(jobId)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_NOT_FOUND));
    jobRepository.delete(existJob);
  }
}
