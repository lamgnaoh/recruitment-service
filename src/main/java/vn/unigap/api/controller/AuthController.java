package vn.unigap.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.unigap.api.dto.in.AuthRequestDto;
import vn.unigap.api.dto.out.APIResponse;
import vn.unigap.api.dto.out.AuthResponseDto;
import vn.unigap.api.service.AuthService;

@RestController
@RequestMapping(value = "api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  @PostMapping("/login")
  public APIResponse<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto authRequestDto) {
    AuthResponseDto dto = authService.login(authRequestDto);
    return APIResponse.success(dto);
  }

}
