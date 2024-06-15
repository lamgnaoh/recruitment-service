package vn.unigap.api.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.unigap.api.dto.in.EmployerCreateRequestDto;
import vn.unigap.api.dto.in.EmployerUpdateRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.EmployerResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.service.EmployerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/employers")
public class EmployerController {

  private final EmployerService employerService;

  @GetMapping
  public ResponseEntity<APIResponse<PageResponse<EmployerResponseDto>>> getAll(
      @RequestParam @Min(value = 0, message = "page must greater than 0") Integer page,
      @Max(value = 500, message = "pageSize must not greater than 500") @RequestParam Integer pageSize) {
    PageResponse<EmployerResponseDto> response = employerService.getAll(page, pageSize);
    return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
  }


  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<?>> getEmployer(@PathVariable Integer id) {
    EmployerResponseDto employerDto = employerService.get(id);
    return new ResponseEntity<>(APIResponse.builder().statusCode(200).object(employerDto).build(),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<APIResponse<?>> createEmployer(
      @Valid @RequestBody EmployerCreateRequestDto employerCreateRequestDto, BindingResult result) {
    if (result.hasErrors()) {
      String errorMessage = result.getFieldErrors().stream()
          .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
          .collect(Collectors.joining(", "));
      return new ResponseEntity<>(APIResponse.error(errorMessage, HttpStatus.BAD_REQUEST, 400),
          HttpStatus.BAD_REQUEST);
    }

    employerService.createEmployer(employerCreateRequestDto);
    return new ResponseEntity<>(APIResponse.builder().statusCode(201).build(), HttpStatus.CREATED);

  }

  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<?>> updateEmployer(@PathVariable Integer id,
      @Valid @RequestBody EmployerUpdateRequestDto employerUpdateRequestDto, BindingResult result) {
    if (result.hasErrors()) {
      String errorMessage = result.getFieldErrors().stream()
          .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
          .collect(Collectors.joining(", "));
      return new ResponseEntity<>(APIResponse.error(errorMessage, HttpStatus.BAD_REQUEST, 400),
          HttpStatus.BAD_REQUEST);
    }

    employerService.update(id, employerUpdateRequestDto);
    return new ResponseEntity<>(APIResponse.builder().statusCode(200).build(), HttpStatus.OK);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<?>> deleteEmployer(@PathVariable Integer id) {
    employerService.delete(id);
    return new ResponseEntity<>(APIResponse.builder().statusCode(200).build(), HttpStatus.OK);

  }


}
