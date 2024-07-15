package vn.unigap.api.util;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncodingException;
import org.springframework.stereotype.Component;
import vn.unigap.api.entity.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
  @Value("${jwt.expiration}")
  private Long expiration;

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private Clock clock = DefaultClock.INSTANCE;


  public String generateToken(UserDetails userDetails) {
    long iat = System.currentTimeMillis() / 1000;
    long exp = iat + Duration.ofHours(expiration).toSeconds();

    JwtEncoderParameters parameters = JwtEncoderParameters.from(
        JwsHeader.with(SignatureAlgorithm.RS256).build(),
            JwtClaimsSet.builder().subject(userDetails.getUsername())
                    .issuedAt(Instant.ofEpochSecond(iat))
                    .expiresAt(Instant.ofEpochSecond(exp))
                    .claim("user_name", userDetails.getUsername())
                    .build());
    try {
      return jwtEncoder.encode(parameters).getTokenValue();
    } catch (JwtEncodingException e) {
      log.error("Error: ", e);
      throw new RuntimeException(e);
    }
  }

  private Map<String, Object> getAllClaimsFromToken(String token) {
    try{
      return jwtDecoder.decode(token).getClaims();
    } catch (Exception e) {
      log.error("Error when parse jwt token : ", e);
      throw new RuntimeException(e);
    }
  }

  public <T> T getClaimFromToken(String token, Function<Map<String, Object>, T> claimsResolver){
    final Map<String, Object> claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, claims -> claims.get("user_name").toString());
  }


  public boolean validateToken(String token, UserDetails userDetails) {
    User user = (User) userDetails;
    String username = getUsernameFromToken(token);
    Date created = getIssuedAtDateFromToken(token);
    return (username.equals(user.getUsername())
        && !isTokenExpired(token));
  }
  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, claim -> Date.from((Instant) claim.get("exp")));
  }
  private Boolean isTokenExpired(String token) {
    Date expire = getExpirationDateFromToken(token);
    return expire.before(clock.now());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, claim -> Date.from((Instant) claim.get("exp")));
  }

}
