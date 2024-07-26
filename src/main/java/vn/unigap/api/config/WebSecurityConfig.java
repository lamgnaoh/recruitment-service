package vn.unigap.api.config;

import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.unigap.api.security.JwtAuthenticationEntryPoint;
import vn.unigap.api.security.JwtAuthorizationTokenFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig {


  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final JwtAuthorizationTokenFilter authenticationTokenFilter;


  @Bean
  public PasswordEncoder bcryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(bcryptPasswordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(){
    return new ProviderManager(daoAuthenticationProvider());
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .authorizeHttpRequests(auth ->
            auth.requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/v1/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/index.html").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());
//    httpSecurity.oauth2ResourceServer(
//        oauth2-> oauth2.jwt( jwtConfigurer -> {
//          try {
//            jwtConfigurer.decoder(NimbusJwtDecoder
//                .withPublicKey(readPublicKey(new ClassPathResource("public.pem"))).build());
//          } catch (Exception e) {
//            throw new RuntimeException(e);
//          }
//        }
//    ));
    httpSecurity.authenticationProvider(daoAuthenticationProvider());
    httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  public static RSAPublicKey readPublicKey(Resource resource) throws Exception {
    return RsaKeyConverters.x509().convert(resource.getInputStream());
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(HttpMethod.GET,
            "/",
            "/*.html",
            "/favicon.ico",
            "/**.html",
            "/**.css",
            "/**.js");
  }

}

