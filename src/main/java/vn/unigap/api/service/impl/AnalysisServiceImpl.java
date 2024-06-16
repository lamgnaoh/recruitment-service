package vn.unigap.api.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.out.AnalysisDto;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.JobReposiroty;
import vn.unigap.api.repository.ResumeRepository;
import vn.unigap.api.repository.SeekerRepository;
import vn.unigap.api.service.AnalysisService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

  private final EmployerRepository employerRepository;
  private final JobReposiroty jobReposiroty;
  private final SeekerRepository seekerRepository;
  private final ResumeRepository resumeRepository;

  @Override
  public AnalysisDto analysis(LocalDate fromDate, LocalDate toDate) {
    LocalDateTime start = fromDate.atStartOfDay();
    LocalDateTime end = toDate.atStartOfDay().plusDays(1);
    Integer numEmployers = employerRepository.countByCreateDateBetween(start, end);
    Integer numJobs = jobReposiroty.countByCreateDateBetween(start, end);
    Integer numSeekers = seekerRepository.countByCreateDateBetween(start, end);
    Integer numResumes = resumeRepository.countByCreateDateBetween(start, end);
    List<AnalysisDto.AnalysisPerDayDto> chart = new ArrayList<>();

    while(!fromDate.isAfter(toDate)){
      LocalDateTime startOfDay = fromDate.atStartOfDay();
      LocalDateTime endOfDay = fromDate.atStartOfDay().plusDays(1);
      Integer numEmployer = employerRepository.countByCreateDateBetween(startOfDay, endOfDay);
      Integer numJob = jobReposiroty.countByCreateDateBetween(startOfDay, endOfDay);
      Integer numSeeker = seekerRepository.countByCreateDateBetween(startOfDay, endOfDay);
      Integer numResume = resumeRepository.countByCreateDateBetween(startOfDay, endOfDay);
      AnalysisDto.AnalysisPerDayDto perDayDto = AnalysisDto.AnalysisPerDayDto.builder()
          .date(fromDate)
          .numEmployer(numEmployer)
          .numJob(numJob)
          .numSeeker(numSeeker)
          .numResume(numResume)
          .build();
      chart.add(perDayDto);
      fromDate = fromDate.plusDays(1);
    }
    return AnalysisDto.builder()
        .numEmployers(numEmployers)
        .numJobs(numJobs)
        .numSeekers(numSeekers)
        .numResumes(numResumes)
        .chart(chart)
        .build();
  }
}
