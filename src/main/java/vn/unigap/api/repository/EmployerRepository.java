package vn.unigap.api.repository;

import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {

  Optional<Employer> findByEmail(String email);

  Integer countByCreateDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
  @Nonnull
  @Cacheable(value = "employer", key = "#id")
  Optional<Employer> findById(@Nonnull Integer id);

  @Nonnull
  @CachePut(value = "employer" , key = "#employer.id")
  Employer save(@Nonnull Employer employer);

  @CachePut(value = "employer" , key = "#employer.id")
  void delete(@Nonnull Employer employer);
}
