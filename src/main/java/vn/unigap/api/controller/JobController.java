package vn.unigap.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.JobResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.service.JobService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/jobs")
public class JobController {


  private final JobService jobService;

  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<JobResponseDto>> getJob(
      @PathVariable("id") Integer jobId
  ){
    JobResponseDto response = jobService.get(jobId);
    return ResponseEntity.ok(APIResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<APIResponse<PageResponse<JobResponseDto>>> getJobs(
       @RequestParam(defaultValue = "-1")  Integer employerId,
      @RequestParam @Min(value = 1, message = "page must greater than 0") Integer page,
      @Max(value = 500, message = "pageSize must not greater than 500") @RequestParam Integer pageSize) {
    PageResponse<JobResponseDto> jobResponseDtos = jobService.getAll(employerId, page, pageSize);
    return ResponseEntity.ok(APIResponse.success(jobResponseDtos));
  }

  @PostMapping
  public ResponseEntity<APIResponse<?>> createJob(
      @Valid @RequestBody JobCreateRequestDto jobCreateRequestDto) {
    jobService.create(jobCreateRequestDto);
    return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED,null), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<?>> updateJob(@PathVariable("id") Integer jobId,
      @Valid @RequestBody JobUpdateRequestDto jobUpdateRequestDto) {
    jobService.update(jobId, jobUpdateRequestDto);
    return ResponseEntity.ok(APIResponse.success( null));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<?>> delete(@PathVariable("id") Integer jobId) {
    jobService.delete(jobId);
    return ResponseEntity.ok(APIResponse.success( null));
  }

}
