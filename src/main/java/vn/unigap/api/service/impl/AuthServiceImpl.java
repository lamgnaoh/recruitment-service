package vn.unigap.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.AuthRequestDto;
import vn.unigap.api.dto.out.AuthResponseDto;
import vn.unigap.api.enums.ErrorCode;
import vn.unigap.api.exception.ApiException;
import vn.unigap.api.util.JwtUtils;
import vn.unigap.api.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService customeUserDetailService;
  private final JwtUtils jwtUtils;

  @Override
  public AuthResponseDto login(AuthRequestDto authRequestDto) {
    UsernamePasswordAuthenticationToken upToken =
        new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword());
    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(upToken);
    } catch (DisabledException e) {
      throw new ApiException(ErrorCode.USER_DISABLED);
    } catch (BadCredentialsException e) {
      throw new ApiException(ErrorCode.BAD_CREDENTIALS);
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = customeUserDetailService.loadUserByUsername(
        authRequestDto.getUsername());
    String token = jwtUtils.generateToken(userDetails);
    return AuthResponseDto.builder().token(token).build();
  }


}
