package vn.unigap.api.repository;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.JobField;

public interface JobFieldRepository extends JpaRepository<JobField, Integer> {

  List<JobField> findByIdIn(List<Integer> id);

  @Nonnull
  @Cacheable("jobField")
  Optional<JobField> findById(@Nonnull Integer id);

}
