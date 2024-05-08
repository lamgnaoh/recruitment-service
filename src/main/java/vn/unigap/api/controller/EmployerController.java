package vn.unigap.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.unigap.api.dto.in.EmployerRequestDTO;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.entity.Employer;
import vn.unigap.api.service.EmployerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/employers")
public class EmployerController {

  private final EmployerService employerService;

  @PostMapping
  public ResponseEntity<APIResponse<?>> createEmployer(
      @Valid @RequestBody EmployerRequestDTO employerRequestDTO
  ) {
    Employer employer = employerService.createEmployer(employerRequestDTO);
    return new ResponseEntity<>(APIResponse.builder().statusCode(201).build(), HttpStatus.CREATED);

  }


}
