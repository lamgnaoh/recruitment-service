package vn.unigap.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
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
import vn.unigap.api.dto.in.SeekerRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.SeekerResponseDto;
import vn.unigap.api.service.SeekerService;

@RestController
@RequestMapping(path = "api/v1/seekers")
@RequiredArgsConstructor
public class SeekerController {

  private final SeekerService seekerService;

  @GetMapping("/{id}")
  public ResponseEntity<APIResponse<SeekerResponseDto>> getJob(
      @PathVariable("id") Integer seekerId
  ){
    SeekerResponseDto response = seekerService.get(seekerId);
    return ResponseEntity.ok(APIResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<APIResponse<List<SeekerResponseDto>>> getSeekers(
      @RequestParam(defaultValue = "-1")  Integer provinceId,
      @RequestParam @Min(value = 1, message = "page must greater than 0") Integer page,
      @Max(value = 500, message = "pageSize must not greater than 500") @RequestParam Integer pageSize) {
    List<SeekerResponseDto> seekerResponseDtos = seekerService.getAll(provinceId, page, pageSize);
    return ResponseEntity.ok(APIResponse.success(seekerResponseDtos));
  }

  @PostMapping
  public ResponseEntity<APIResponse<?>> createSekker(
      @Valid @RequestBody SeekerRequestDto seekerRequestDto) {
    seekerService.create(seekerRequestDto);
    return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED,null), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<APIResponse<?>> updateSeeker(@PathVariable("id") Integer seekerId,
      @Valid @RequestBody SeekerRequestDto seekerRequestDto) {
    seekerService.update(seekerId, seekerRequestDto);
    return ResponseEntity.ok(APIResponse.success( null));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<APIResponse<?>> deleteSeeker(@PathVariable("id") Integer seekerId) {
    seekerService.delete(seekerId);
    return ResponseEntity.ok(APIResponse.success( null));

  }


}
