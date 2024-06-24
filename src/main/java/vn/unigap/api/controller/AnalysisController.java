package vn.unigap.api.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.AnalysisDto;
import vn.unigap.api.service.AnalysisService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analysis")
public class AnalysisController {

  private final AnalysisService analysisService;

  @GetMapping
  public ResponseEntity<APIResponse<AnalysisDto>> getCommonStatistics(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
    AnalysisDto dto = analysisService.analysis(fromDate, toDate);
    return ResponseEntity.ok(APIResponse.success(dto));
  }

}
