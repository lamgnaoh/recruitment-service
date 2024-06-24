package vn.unigap.api.repository;


import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Job;


public interface JobReposiroty extends JpaRepository<Job, Integer> {

  Page<Job> findAllByEmployerId(Integer employerId, Pageable pageable);

  Integer countByCreateDateBetween(LocalDateTime start, LocalDateTime end);

  @Nonnull
  @Cacheable(value = "job")
  Optional<Job> findById(@Nonnull Integer id);

  @CacheEvict(value = "job", key = "#job.id")
  void delete(@Nonnull Job job);

  @Nonnull
  @CachePut(value = "job", key = "#job.id")
  Job save(@Nonnull Job job);
}
