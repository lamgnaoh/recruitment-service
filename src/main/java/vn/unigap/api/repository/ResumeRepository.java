package vn.unigap.api.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

  Page<Resume> findAllBySeekerId(Integer seekerId , Pageable pageable);

  Integer countByCreateDateBetween(LocalDateTime start, LocalDateTime end);
}
