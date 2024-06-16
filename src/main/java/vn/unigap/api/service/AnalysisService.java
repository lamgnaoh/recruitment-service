package vn.unigap.api.service;

import java.time.LocalDate;
import vn.unigap.api.dto.out.AnalysisDto;

public interface AnalysisService {

  AnalysisDto analysis(LocalDate fromDate, LocalDate toDate);

}
