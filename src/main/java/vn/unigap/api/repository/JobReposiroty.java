package vn.unigap.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Job;


public interface JobReposiroty extends JpaRepository<Job, Integer> {

  Page<Job> findAllByEmployerId(Integer employerId, Pageable pageable);
}
