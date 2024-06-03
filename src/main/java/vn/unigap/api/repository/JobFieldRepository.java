package vn.unigap.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.JobField;

public interface JobFieldRepository extends JpaRepository<JobField, Integer> {

  List<JobField> findByIdIn(List<Integer> id);

}
