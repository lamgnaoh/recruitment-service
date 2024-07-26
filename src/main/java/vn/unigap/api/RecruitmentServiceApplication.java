package vn.unigap.api;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableSentry(dsn = "https://619fcaa59075f6f59280f5c457c116a7@o4507657919528960.ingest.de.sentry.io/4507657924575312")
public class RecruitmentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RecruitmentServiceApplication.class, args);
  }

}
