package vn.unigap.api.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import vn.unigap.api.dto.in.JobCreateRequestDto;
import vn.unigap.api.dto.in.JobUpdateRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.JobResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void getAll() throws Exception {
    int page = 1;
    int pageSize = 10;

    var uri = "/api/v1/jobs?page=" + page + "&pageSize=" + pageSize ;
    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk())
        .andExpect(result -> {
          var response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<APIResponse<PageResponse<JobResponseDto>>>() {
              });

          Assertions.assertNotNull(response);
          Assertions.assertNotNull(response.getObject());
          Assertions.assertNotNull(response.getObject().getData());
          Long totalElements = response.getObject().getTotalElements();
          Long totalPages = response.getObject().getTotalPages();
          long expectedTotalPages =
              totalElements / pageSize + (totalElements % pageSize == 0 ? 0 : 1);
          assertEquals(expectedTotalPages, totalPages);
          long expectedDataSize = pageSize;
          if (page > totalPages) {
            expectedDataSize = 0L;
          }
          if (page < totalPages) {
            expectedDataSize = pageSize;
          }

          if ((long) page == totalPages) {
            if (totalElements % pageSize == 0) {
              expectedDataSize = pageSize;
            } else {
              expectedDataSize = totalElements % pageSize;
            }
          }

          assertEquals(expectedDataSize, response.getObject().getData().size());
        });
  }

  @Test
  void getJobFail() throws Exception {
    int jobId = 1;
    var uri = "/api/v1/jobs/" + jobId;

    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().is4xxClientError())
        .andExpect(result -> assertInstanceOf(ApiException.class, result.getResolvedException()))
        .andExpect(result -> assertEquals(ErrorCode.JOB_NOT_FOUND.getMessage(),
            Objects.requireNonNull((ApiException) result.getResolvedException()).getErrorCode()
                .getMessage()));

  }

  @Test
  void getJobSuccess() throws Exception {
    int jobId = 4488937;
    var uri = "/api/v1/jobs/" + jobId;
    JobResponseDto expectedJob = JobResponseDto.builder().id(jobId)
        .title("Nhân Viên Bán Hàng Trực Tiếp - Direct Sales Agent")
        .quantity(20)
        .salary(BigDecimal.valueOf(6))
        .build();

    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk())
        .andExpect(result -> {
          var response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<APIResponse<JobResponseDto>>() {
              });
          assertEquals(response.getObject().getId(), expectedJob.getId());
          assertEquals(response.getObject().getSalary(), expectedJob.getSalary());
        });

  }

  @Test
  @Transactional
  void createJobSuccess() throws Exception {
    JobCreateRequestDto jobCreateRequestDto = JobCreateRequestDto.builder()
        .title("Job 1")
        .employerId(3093561)
        .quantity(5)
        .description("Job 1 description")
        .fieldIds(List.of(13,23,33))
        .provinceIds(List.of(1,2))
        .salary(BigDecimal.valueOf(5000))
        .expiredAt(LocalDateTime.now()).build();

    String json = objectMapper.writeValueAsString(jobCreateRequestDto);

    this.mockMvc.perform(
            post("/api/v1/jobs").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void createJobFail() throws Exception {
    JobCreateRequestDto jobCreateRequestDto = JobCreateRequestDto.builder()
        .title("Job 1")
        .employerId(1)
        .quantity(5)
        .description("Job 1 description")
        .fieldIds(List.of(13,23,33))
        .provinceIds(List.of(1,2))
        .salary(BigDecimal.valueOf(5000))
        .expiredAt(LocalDateTime.now()).build();

    String json = objectMapper.writeValueAsString(jobCreateRequestDto);

    this.mockMvc.perform(
            post("/api/v1/jobs").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError());


  }

  @Test
  void updateJobFail() throws Exception {
    int jobId = 1;
    var uri = "/api/v1/jobs/" + jobId;

    JobUpdateRequestDto jobUpdateRequestDto = JobUpdateRequestDto.builder()
        .title("Job 1")
        .quantity(5)
        .description("Job 1 description")
        .fieldIds(List.of(13,23,33))
        .provinceIds(List.of(1,2))
        .salary(BigDecimal.valueOf(5000))
        .expiredAt(LocalDateTime.now()).build();
    String json = objectMapper.writeValueAsString(jobUpdateRequestDto);

    this.mockMvc.perform(
            MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError())
        .andExpect(result -> assertInstanceOf(ApiException.class, result.getResolvedException()))
        .andExpect(result -> assertEquals(ErrorCode.JOB_NOT_FOUND.getMessage(),
            Objects.requireNonNull((ApiException) result.getResolvedException()).getErrorCode()
                .getMessage()));
  }

  @Test
  @Transactional
  void updateJobSuccess() throws Exception {
    int jobId = 4488937;
    var uri = "/api/v1/jobs/" + jobId;

    JobUpdateRequestDto jobUpdateRequestDto = JobUpdateRequestDto.builder()
        .title("Job 1")
        .quantity(5)
        .description("Job 1 description")
        .fieldIds(List.of(13,23,33))
        .provinceIds(List.of(1,2))
        .salary(BigDecimal.valueOf(5000))
        .expiredAt(LocalDateTime.now()).build();
    String json = objectMapper.writeValueAsString(jobUpdateRequestDto);

    this.mockMvc.perform(
            MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void deleteJobSuccess() throws Exception {
    int jobId = 4488937;
    var uri = "/api/v1/jobs/" + jobId;
    this.mockMvc.perform(
            MockMvcRequestBuilders.delete(uri))
        .andExpect(status().isOk());

  }

}
