package vn.unigap.api.dto.out;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisDto {
  private Integer numEmployers;
  private Integer numJobs;
  private Integer numSeekers;
  private Integer numResumes;
  private List<AnalysisPerDayDto> chart;


  @Data
  @Builder
  public static class AnalysisPerDayDto {
    private LocalDate date;
    private Integer numEmployer;
    private Integer numJob;
    private Integer numSeeker;
    private Integer numResume;
  }
}
