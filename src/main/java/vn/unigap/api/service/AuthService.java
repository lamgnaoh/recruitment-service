package vn.unigap.api.service;

import vn.unigap.api.dto.in.AuthRequestDto;
import vn.unigap.api.dto.out.AuthResponseDto;

public interface AuthService {

  AuthResponseDto login(AuthRequestDto authRequestDto);
}
