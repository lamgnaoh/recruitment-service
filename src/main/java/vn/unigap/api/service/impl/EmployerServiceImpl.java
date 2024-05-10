package vn.unigap.api.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.EmployerCreateRequestDto;
import vn.unigap.api.dto.in.EmployerUpdateRequestDto;
import vn.unigap.api.dto.out.EmployerResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.entity.Employer;
import vn.unigap.api.entity.Province;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.ProvinceRepository;
import vn.unigap.api.service.EmployerService;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

  private final EmployerRepository employerRepository;
  private final ProvinceRepository provinceRepository;

  @Override
  public Employer createEmployer(EmployerCreateRequestDto employerCreateRequestDto) {
    Employer employer = new Employer();
    String email = employerCreateRequestDto.getEmail();
    Integer provinceId = employerCreateRequestDto.getProvinceId();
    Optional<Employer> employerByEmail = employerRepository.findByEmail(email);
    if (employerByEmail.isPresent()){
      throw new ApiException(ErrorCode.EMAIL_ALREADY_EXIST);
    }
    provinceRepository.findById(provinceId).orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    employer.setEmail(email);
    employer.setName(employerCreateRequestDto.getName());
    employer.setProvince(employerCreateRequestDto.getProvinceId());
    employer.setDescription(employer.getDescription());
    return employerRepository.save(employer);
  }

  @Override
  public Employer update(Integer employerId, EmployerUpdateRequestDto employerUpdateRequestDto) {
    Employer employer = employerRepository.findById(employerId)
        .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYER_NOT_FOUND));
    employer.setName(employerUpdateRequestDto.getName());
    employer.setProvince(employerUpdateRequestDto.getProvinceId());
    if (employerUpdateRequestDto.getDescription() != null
        && !employerUpdateRequestDto.getDescription().isEmpty()) {
      employer.setDescription(employerUpdateRequestDto.getDescription());
    }
    return employerRepository.save(employer);
  }

  @Override
  public EmployerResponseDto get(Integer employerId) {
    Employer employer = employerRepository.findById(employerId)
        .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYER_NOT_FOUND));
    Province province = provinceRepository.findById(employer.getProvince())
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    return EmployerResponseDto.builder()
        .email(employer.getEmail())
        .name(employer.getName())
        .provinceId(employer.getProvince())
        .name(province.getName())
        .build();
  }

  @Override
  public PageResponse<EmployerResponseDto> getAll(Integer page, Integer pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name"));
    Page<Employer> allEmployers = employerRepository.findAll(pageable);
    List<EmployerResponseDto> employerResponseDtos = allEmployers.getContent().stream().map(
        employer -> EmployerResponseDto.builder()
            .id(employer.getId())
            .email(employer.getEmail())
            .name(employer.getName()).provinceId(employer.getId())
            .provinceName(
                provinceRepository.findById(employer.getProvince())
                    .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND)).getName()
            )
            .build()).toList();
    return PageResponse.<EmployerResponseDto>builder().page(page).pageSize(pageSize)
        .totalElements(allEmployers.getTotalElements())
        .totalPages((long) allEmployers.getTotalPages()).data(employerResponseDtos).build();
  }

  @Override
  public Employer delete(Integer employerId) {
    Employer employer = employerRepository.findById(employerId)
        .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYER_NOT_FOUND));
    employerRepository.delete(employer);
    return employer;
  }


}
