package vn.unigap.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.AppUtils;
import vn.unigap.api.dto.in.ResumeCreateRequestDto;
import vn.unigap.api.dto.in.ResumeUpdateRequestDto;
import vn.unigap.api.dto.out.FieldDto;
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
    provinceIds.stream().map(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND))).toList();
    List<Integer> fieldIds = resumeCreateRequestDto.getFieldIds();
    fieldIds.stream().map(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND))).toList();

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
    provinceIds.stream().map(id -> provinceRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND))).toList();
    List<Integer> fieldIds = resumeUpdateRequestDto.getFieldIds();
    fieldIds.stream().map(id -> jobFieldRepository.findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.JOB_FIELD_NOT_FOUND))).toList();
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
    List<FieldDto> fieldDtos = appUtils.getFieldDtoFromIds(
        savedResume.getFields().substring(1, savedResume.getFields().length() - 1).split("-"));

    List<ProvinceDto> provinceDtos = appUtils.getProvinceDtoFromIds(
        savedResume.getProvinces().substring(1, savedResume.getFields().length() - 1).split("-"));

    return ResumeResponseDto.builder().id(savedResume.getId())
        .seekerId(savedResume.getSeeker().getId()).seekerName(savedResume.getSeeker().getName())
        .careerObj(savedResume.getCareerObj()).title(savedResume.getTitle())
        .salary(savedResume.getSalary()).fields(fieldDtos).provinces(provinceDtos).build();
  }

  @Override
  public List<ResumeResponseDto> getAll(Integer seekerId, Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    List<Resume> resumes;
    if (seekerId == -1) {
      resumes = resumeRepository.findAll(pageable).get().toList();
    } else {
      resumes = resumeRepository.findAllBySeekerId(seekerId, pageable).get().toList();
    }
    return resumes.stream().map(resume -> ResumeResponseDto.builder()
        .id(resume.getId())
        .seekerId(resume.getSeeker().getId())
        .seekerName(resume.getSeeker().getName())
        .careerObj(resume.getCareerObj())
        .title(resume.getTitle())
        .salary(resume.getSalary())
        .build()).toList();
  }

  @Override
  public void delete(Integer resumeId) {
    Resume savedResume = resumeRepository.findById(resumeId)
        .orElseThrow(() -> new ApiException(ErrorCode.RESUME_NOT_FOUND));
    resumeRepository.delete(savedResume);
  }

}
