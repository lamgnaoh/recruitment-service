package vn.unigap.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordEncoderUtil {

  public static void main(String[] args) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    String rawPassword = "123456aA@";
    String encodedPassword = encoder.encode(rawPassword);
    log.info("Encoded password: {}", encodedPassword);
  }

}
