package vn.unigap.api.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.SeekerRequestDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.dto.out.SeekerResponseDto;
import vn.unigap.api.entity.Province;
import vn.unigap.api.entity.Seeker;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;
import vn.unigap.api.repository.ProvinceRepository;
import vn.unigap.api.repository.SeekerRepository;
import vn.unigap.api.service.SeekerService;

@Service
@RequiredArgsConstructor
public class SeekerServiceImpl implements SeekerService {

  private final SeekerRepository seekerRepository;
  private final ProvinceRepository provinceRepository;

  @Override
  public void create(SeekerRequestDto seekerRequestDto) {
    Province province = provinceRepository.findById(seekerRequestDto.getProvinceId())
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    Seeker seeker = Seeker.builder().name(seekerRequestDto.getName())
        .birthday(seekerRequestDto.getBirthday()).address(seekerRequestDto.getAddress())
        .province(province).build();
    seekerRepository.save(seeker);
  }

  @Override
  @CachePut(value = "seeker" , key = "#seekerId")
  public void update(Integer seekerId, SeekerRequestDto seekerRequestDto) {
    Seeker savedSeeker = seekerRepository.findById(seekerId)
        .orElseThrow(() -> new ApiException(ErrorCode.SEEKER_NOT_FOUND));
    Province province = provinceRepository.findById(seekerRequestDto.getProvinceId())
        .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    if (seekerRequestDto.getName() != null) {
      savedSeeker.setName(seekerRequestDto.getName());
    }
    if (seekerRequestDto.getBirthday() != null) {
      savedSeeker.setBirthday(seekerRequestDto.getBirthday());
    }

    if (seekerRequestDto.getAddress() != null) {
      savedSeeker.setAddress(seekerRequestDto.getAddress());
    }

    if (seekerRequestDto.getProvinceId() != null) {
      savedSeeker.setProvince(province);
    }
    seekerRepository.save(savedSeeker);
  }

  @Override
  @Cacheable("seeker")
  public SeekerResponseDto get(Integer seekerId) {
    Seeker seeker = seekerRepository.findById(seekerId)
        .orElseThrow(() -> new ApiException(ErrorCode.SEEKER_NOT_FOUND));
    Province province;
    if (seeker.getProvince() != null){
      province = provinceRepository.findById(seeker.getProvince().getId())
          .orElseThrow(() -> new ApiException(ErrorCode.PROVINCE_NOT_FOUND));
    } else {
      province = null;
    }
    return SeekerResponseDto.builder().id(seeker.getId()).name(seeker.getName())
        .birthday(seeker.getBirthday()).address(seeker.getAddress())
        .provinceId(seeker.getProvince() != null ? seeker.getProvince().getId() : null)
        .provinceName(province != null ? province.getName() : null).build();
  }

  @Override
  public PageResponse<SeekerResponseDto> getAll(Integer provinceId, Integer page,
      Integer pageSize) {
    Pageable paging = PageRequest.of(page - 1, pageSize);
    Page<Seeker> seekers;
    if (provinceId == -1) {
      seekers = seekerRepository.findAll(paging);
    } else {
      seekers = seekerRepository.findAllByProvinceId(provinceId, paging);
    }
    List<SeekerResponseDto> seekerResponseDtos = seekers.getContent().stream().map(
        seeker -> SeekerResponseDto.builder().id(seeker.getId()).name(seeker.getName())
            .birthday(seeker.getBirthday()).address(seeker.getAddress())
            .provinceId(seeker.getProvince() != null ? seeker.getProvince().getId() : null)
            .provinceName(seeker.getProvince() != null ? seeker.getProvince().getName() : null)
            .build()).toList();
    return PageResponse.<SeekerResponseDto>builder().page(page).pageSize(pageSize)
        .totalElements(seekers.getTotalElements()).totalPages((long) seekers.getTotalPages())
        .data(seekerResponseDtos).build();


  }

  @Override
  @CacheEvict(value = "seeker", key = "#seekerId")
  public void delete(Integer seekerId) {
    Seeker savedSeeker = seekerRepository.findById(seekerId)
        .orElseThrow(() -> new ApiException(ErrorCode.SEEKER_NOT_FOUND));
    seekerRepository.delete(savedSeeker);
  }
}
