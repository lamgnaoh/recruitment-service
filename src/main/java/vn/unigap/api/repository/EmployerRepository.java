package vn.unigap.api.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {

  Optional<Employer> findByEmail(String email);

  Integer countByCreateDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
