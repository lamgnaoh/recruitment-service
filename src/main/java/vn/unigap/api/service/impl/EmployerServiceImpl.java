package vn.unigap.api.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.EmployerRequestDTO;
import vn.unigap.api.entity.Employer;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.service.EmployerService;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

  private final EmployerRepository employerRepository;

  @Override
  public Employer createEmployer(EmployerRequestDTO employerRequestDTO) {
    Employer employer = new Employer();
    String email = employerRequestDTO.getEmail();
    Optional<Employer> optionalEmployer = employerRepository.findByEmail(email);
    if (optionalEmployer.isPresent()){
      throw new RuntimeException("Email already exists");
    }
    employer.setEmail(email);
    employer.setName(employerRequestDTO.getName());
    employer.setProvince(employerRequestDTO.getProvinceId());
    employer.setDescription(employer.getDescription());
    return employerRepository.save(employer);
  }
}
