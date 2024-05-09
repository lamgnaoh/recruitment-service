package vn.unigap.api.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.EmployerRequestDTO;
import vn.unigap.api.entity.Employer;
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
  public Employer createEmployer(EmployerRequestDTO employerRequestDTO) {
    Employer employer = new Employer();
    String email = employerRequestDTO.getEmail();
    Integer provinceId = employerRequestDTO.getProvinceId();
    Optional<Employer> employerByEmail = employerRepository.findByEmail(email);
    if (employerByEmail.isPresent()){
      throw new ApiException(ErrorCode.EMAIL_ALREADY_EXIST);
    }
    provinceRepository.findById(provinceId).orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    employer.setEmail(email);
    employer.setName(employerRequestDTO.getName());
    employer.setProvince(employerRequestDTO.getProvinceId());
    employer.setDescription(employer.getDescription());
    return employerRepository.save(employer);
  }
}
