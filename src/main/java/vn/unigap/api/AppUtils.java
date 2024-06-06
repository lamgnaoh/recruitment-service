package vn.unigap.api;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.unigap.api.dto.out.FieldDto;
import vn.unigap.api.dto.out.ProvinceDto;
import vn.unigap.api.entity.JobField;
import vn.unigap.api.entity.Province;
import vn.unigap.api.repository.JobFieldRepository;
import vn.unigap.api.repository.ProvinceRepository;


@Component
@RequiredArgsConstructor
public class AppUtils {
  private final JobFieldRepository jobFieldRepository;
  private final ProvinceRepository provinceRepository;

  public List<FieldDto> getFieldDtoFromIds(String[] fieldIds){
    List<JobField> jobFields = jobFieldRepository.findByIdIn(
        Arrays.stream(fieldIds).map(Integer::valueOf).toList());
    return jobFields.stream()
        .map(el -> FieldDto.builder().id(el.getId()).name(el.getField()).build()).toList();
  }

  public List<ProvinceDto> getProvinceDtoFromIds(String[] provinceIds){
    List<Province> provinceDtos = provinceRepository.findByIdIn(
        Arrays.stream(provinceIds).map(Integer::valueOf).toList());
    return provinceDtos.stream()
        .map(el -> ProvinceDto.builder().id(el.getId()).name(el.getName()).build()).toList();
  }
}
