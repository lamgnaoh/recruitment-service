package vn.unigap.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Seeker;

public interface SeekerRepository extends JpaRepository<Seeker,Integer> {

  List<Seeker> findAllByProvinceId(Integer provinceId);
}
