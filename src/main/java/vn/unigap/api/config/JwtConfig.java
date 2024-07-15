package vn.unigap.api.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@Slf4j
public class JwtConfig {
  @Bean
  public JwtEncoder jwtEncoder() {
    try {
      return new NimbusJwtEncoder(new ImmutableJWKSet<>(
          new JWKSet(new RSAKey.Builder(readPublicKey(new ClassPathResource("public.pem")))
              .privateKey(readPrivateKey(new ClassPathResource("private.pem"))).build())));
    } catch (Exception e) {
      log.error("Error: ", e);
      throw new RuntimeException(e);
    }
  }

  @Bean
  public JwtDecoder jwtDecoder() throws Exception {
    return NimbusJwtDecoder
        .withPublicKey(readPublicKey(new ClassPathResource("public.pem"))).build();
  }

  public static RSAPublicKey readPublicKey(Resource resource) throws Exception {
    return RsaKeyConverters.x509().convert(resource.getInputStream());
  }

  private static RSAPrivateKey readPrivateKey(Resource resource) throws Exception {
    return RsaKeyConverters.pkcs8().convert(resource.getInputStream());
  }
}
