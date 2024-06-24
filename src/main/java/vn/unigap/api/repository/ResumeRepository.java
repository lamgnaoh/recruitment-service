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
import vn.unigap.api.entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

  Page<Resume> findAllBySeekerId(Integer seekerId , Pageable pageable);

  Integer countByCreateDateBetween(LocalDateTime start, LocalDateTime end);

  @Nonnull
  @Cacheable("resume")
  Optional<Resume> findById(@Nonnull Integer id);

  @Nonnull
  @CachePut(value = "resume", key = "#resume.id")
  Resume save(@Nonnull Resume resume);

  @CacheEvict(value = "resume", key = "#resume.id")
  void delete(@Nonnull Resume resume);

}
