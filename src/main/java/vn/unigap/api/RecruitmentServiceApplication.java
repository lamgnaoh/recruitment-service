package vn.unigap.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class RecruitmentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RecruitmentServiceApplication.class, args);
  }

}
