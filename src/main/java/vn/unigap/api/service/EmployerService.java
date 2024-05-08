package vn.unigap.api.service;


import vn.unigap.api.dto.in.EmployerRequestDTO;
import vn.unigap.api.entity.Employer;

public interface EmployerService {

  Employer createEmployer(EmployerRequestDTO employerRequestDTO);
}
