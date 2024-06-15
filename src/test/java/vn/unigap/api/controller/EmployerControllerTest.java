package vn.unigap.api.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import vn.unigap.api.dto.in.EmployerCreateRequestDto;
import vn.unigap.api.dto.in.EmployerUpdateRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.EmployerResponseDto;
import vn.unigap.api.dto.out.PageResponse;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void getAll() throws Exception {
    int page = 1;
    int pageSize = 10;

    var uri = "/api/v1/employers?page=" + page + "&pageSize=" + pageSize;

    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk())
        .andExpect(result -> {
          var response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<APIResponse<PageResponse<EmployerResponseDto>>>() {
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
  void getEmployerFail() throws Exception {
    int employerId = 1;
    var uri = "/api/v1/employers/" + employerId;

    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().is4xxClientError())
        .andExpect(result -> assertInstanceOf(ApiException.class, result.getResolvedException()))
        .andExpect(result -> assertEquals(ErrorCode.EMPLOYER_NOT_FOUND.getMessage(),
            Objects.requireNonNull((ApiException) result.getResolvedException()).getErrorCode()
                .getMessage()));

  }

  @Test
  void getEmployerSuccess() throws Exception {
    int employerId = 3093638;
    var uri = "/api/v1/employers/" + employerId;
    EmployerResponseDto expectedEmployer = EmployerResponseDto.builder().id(employerId)
        .email("0f697435f2094b7e7ea5a5702105431ee344c198@sieu-viet.com").name("tc group")
        .provinceId(1).build();

    this.mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk())
        .andExpect(result -> {
          var response = objectMapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<APIResponse<EmployerResponseDto>>() {
              });
          assertEquals(response.getObject().getEmail(), expectedEmployer.getEmail());
          assertEquals(response.getObject().getName(), expectedEmployer.getName());
          assertEquals(response.getObject().getProvinceId(), expectedEmployer.getProvinceId());
        });

  }

  @Test
  void createEmployerFail() throws Exception {
    EmployerCreateRequestDto employerCreateRequestDto = EmployerCreateRequestDto.builder()
        .email("Test").name("Test").provinceId(1).build();

    String json = objectMapper.writeValueAsString(employerCreateRequestDto);

    this.mockMvc.perform(
            post("/api/v1/employers").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError());


  }

  @Test
  @Transactional
  void createEmployerSuccess() throws Exception {
    EmployerCreateRequestDto employerCreateRequestDto = EmployerCreateRequestDto.builder()
        .email("test@gmail.com").name("Test").provinceId(1).build();

    String json = objectMapper.writeValueAsString(employerCreateRequestDto);

    this.mockMvc.perform(
            post("/api/v1/employers").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void updateEmployerFail() throws Exception {
    int employerId = 1;
    var uri = "/api/v1/employers/" + employerId;

    EmployerUpdateRequestDto employerUpdateRequestDto = EmployerUpdateRequestDto.builder()
        .name("Test").provinceId(1).build();
    String json = objectMapper.writeValueAsString(employerUpdateRequestDto);

    this.mockMvc.perform(
            MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError())
        .andExpect(result -> assertInstanceOf(ApiException.class, result.getResolvedException()))
        .andExpect(result -> assertEquals(ErrorCode.EMPLOYER_NOT_FOUND.getMessage(),
            Objects.requireNonNull((ApiException) result.getResolvedException()).getErrorCode()
                .getMessage()));
  }

  @Test
  @Transactional
  void updateEmployerSuccess() throws Exception {
    int employerId = 3094566;
    var uri = "/api/v1/employers/" + employerId;

    EmployerUpdateRequestDto employerUpdateRequestDto = EmployerUpdateRequestDto.builder()
        .name("Viettel").provinceId(2).build();
    String json = objectMapper.writeValueAsString(employerUpdateRequestDto);

    this.mockMvc.perform(
            MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

  }

  @Test
  @Transactional
  void deleteEmployerSuccess() throws Exception {
    int employerId = 3094566;
    var uri = "/api/v1/employers/" + employerId;
    this.mockMvc.perform(
            MockMvcRequestBuilders.delete(uri))
        .andExpect(status().isOk());

  }

}
