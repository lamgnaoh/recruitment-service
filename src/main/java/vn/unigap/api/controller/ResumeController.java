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
import vn.unigap.api.dto.in.ResumeCreateRequestDto;
import vn.unigap.api.dto.in.ResumeUpdateRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.dto.out.ResumeResponseDto;
import vn.unigap.api.service.ResumeService;

@RestController
@RequestMapping(path = "api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

  private final ResumeService resumeService;

  @PostMapping
  public ResponseEntity<APIResponse<?>> createResume(@Valid @RequestBody ResumeCreateRequestDto resumeCreateRequestDto) {
    resumeService.create(resumeCreateRequestDto);
    return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED,null), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<?>> updateResume(
      @PathVariable("id") Integer resumeId,
      @Valid @RequestBody ResumeUpdateRequestDto resumeUpdateRequestDto){
    resumeService.update(resumeId,resumeUpdateRequestDto);
    return ResponseEntity.ok(APIResponse.success(null));
  }

  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<ResumeResponseDto>> get(@PathVariable("id") Integer resumeId){
    ResumeResponseDto resumeResponseDto = resumeService.get(resumeId);
    return ResponseEntity.ok(APIResponse.success(resumeResponseDto));
  }

  @GetMapping
  public ResponseEntity<APIResponse<PageResponse<ResumeResponseDto>>> getAll(
      @RequestParam("seekerId") Integer seekerId,
      @RequestParam @Min(value = 1, message = "page must greater than 0") Integer page,
      @Max(value = 500, message = "pageSize must not greater than 500") @RequestParam Integer pageSize

  ){
    PageResponse<ResumeResponseDto> responseDtos = resumeService.getAll(seekerId, page, pageSize);
    return ResponseEntity.ok(APIResponse.success(responseDtos));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<?>> delete(
      @PathVariable("id")Integer resumeId
  ){
    resumeService.delete(resumeId);
    return ResponseEntity.ok(APIResponse.success(null));
  }



}
