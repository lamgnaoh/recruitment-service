package vn.unigap.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.AppUtils;
import vn.unigap.api.dto.in.ResumeCreateRequestDto;
import vn.unigap.api.dto.in.ResumeUpdateRequestDto;
import vn.unigap.api.dto.out.FieldDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.dto.out.ProvinceDto;
import vn.unigap.api.dto.out.ResumeResponseDto;
import vn.unigap.api.entity.Resume;
import vn.unigap.api.entity.Seeker;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;
import vn.unigap.api.repository.JobFieldRepository;
import vn.unigap.api.repository.ProvinceRepository;
import vn.unigap.api.repository.ResumeRepository;
import vn.unigap.api.repository.SeekerRepository;
import vn.unigap.api.service.ResumeService;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

  private final SeekerRepository seekerRepository;
  private final ProvinceRepository provinceRepository;
  private final JobFieldRepository jobFieldRepository;
  private final ResumeRepository resumeRepository;
  private final AppUtils appUtils;

  @Override
  public void create(ResumeCreateRequestDto resumeCreateRequestDto) {
    Seeker seeker = seekerRepository.findById(resumeCreateRequestDto.getSeekerId())
        .orElseThrow(() -> new ApiException(ErrorCode.SEEKER_NOT_FOUND));

    List<Integer> provinceIds = resumeCreateRequestDto.getProvinceIds();
    provinceIds.forEach(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND)));
    List<Integer> fieldIds = resumeCreateRequestDto.getFieldIds();
    fieldIds.forEach(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND)));

    Resume resume = Resume.builder().seeker(seeker).careerObj(resumeCreateRequestDto.getCareerObj())
        .title(resumeCreateRequestDto.getTitle()).salary(resumeCreateRequestDto.getSalary())
        .fields("-" + fieldIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-")
        .provinces(
            "-" + provinceIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-")
        .build();
    resumeRepository.save(resume);
  }

  @Override
  public void update(Integer resumeId, ResumeUpdateRequestDto resumeUpdateRequestDto) {
    Resume savedResume = resumeRepository.findById(resumeId)
        .orElseThrow(() -> new ApiException(ErrorCode.RESUME_NOT_FOUND));
    List<Integer> provinceIds = resumeUpdateRequestDto.getProvinceIds();
    provinceIds.forEach(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND)));
    List<Integer> fieldIds = resumeUpdateRequestDto.getFieldIds();
    fieldIds.forEach(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND)));
    if (resumeUpdateRequestDto.getCareerObj() != null) {
      savedResume.setCareerObj(resumeUpdateRequestDto.getCareerObj());
    }
    if (resumeUpdateRequestDto.getTitle() != null) {
      savedResume.setTitle(resumeUpdateRequestDto.getTitle());
    }
    if (resumeUpdateRequestDto.getSalary() != null) {
      savedResume.setSalary(resumeUpdateRequestDto.getSalary());
    }
    if (resumeUpdateRequestDto.getFieldIds() != null && !resumeUpdateRequestDto.getFieldIds()
        .isEmpty()) {
      savedResume.setFields(
          "-" + fieldIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-");
    }
    if (resumeUpdateRequestDto.getProvinceIds() != null && !resumeUpdateRequestDto.getProvinceIds()
        .isEmpty()) {
      savedResume.setProvinces(
          "-" + provinceIds.stream().map(String::valueOf).collect(Collectors.joining("-")) + "-");
    }
    resumeRepository.save(savedResume);
  }

  @Override
  public ResumeResponseDto get(Integer resumeId) {
    Resume savedResume = resumeRepository.findById(resumeId)
        .orElseThrow(() -> new ApiException(ErrorCode.RESUME_NOT_FOUND));
    List<FieldDto> fieldDtos;
    if (!savedResume.getFields().equals("0")){
      fieldDtos = appUtils.getFieldDtoFromIds(
          savedResume.getFields().substring(1, savedResume.getFields().length() - 1).split("-"));
    } else {
      fieldDtos = null;
    }

    List<ProvinceDto> provinceDtos;
    if (!savedResume.getProvinces().equals("0")){
      provinceDtos = appUtils.getProvinceDtoFromIds(
          savedResume.getProvinces().substring(1, savedResume.getFields().length() - 1).split("-"));
    }else{
      provinceDtos = null;
    }

    return ResumeResponseDto.builder().id(savedResume.getId())
        .seekerId(savedResume.getSeeker() != null ? savedResume.getSeeker().getId() : null)
        .seekerName(savedResume.getSeeker() != null ? savedResume.getSeeker().getName() : null)
        .careerObj(savedResume.getCareerObj()).title(savedResume.getTitle())
        .salary(savedResume.getSalary()).fields(fieldDtos).provinces(provinceDtos).build();
  }

  @Override
  public PageResponse<ResumeResponseDto> getAll(Integer seekerId, Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    Page<Resume> resumes;
    if (seekerId == -1) {
      resumes = resumeRepository.findAll(pageable);
    } else {
      resumes = resumeRepository.findAllBySeekerId(seekerId, pageable);
    }
    List<ResumeResponseDto> responseDtos = resumes.getContent().stream().map(
        resume -> ResumeResponseDto.builder().id(resume.getId())
            .seekerId(resume.getSeeker() != null ? resume.getSeeker().getId() : null)
            .seekerName(resume.getSeeker() != null ? resume.getSeeker().getName() : null)
            .careerObj(resume.getCareerObj()).title(resume.getTitle()).salary(resume.getSalary())
            .build()).toList();
    return PageResponse.<ResumeResponseDto>builder().page(page).pageSize(pageSize)
        .totalElements(resumes.getTotalElements()).totalPages((long) resumes.getTotalPages())
        .data(responseDtos).build();
  }

  @Override
  public void delete(Integer resumeId) {
    Resume savedResume = resumeRepository.findById(resumeId)
        .orElseThrow(() -> new ApiException(ErrorCode.RESUME_NOT_FOUND));
    resumeRepository.delete(savedResume);
  }

}
