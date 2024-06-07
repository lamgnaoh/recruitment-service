package vn.unigap.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Seeker;

public interface SeekerRepository extends JpaRepository<Seeker,Integer> {

  Page<Seeker> findAllByProvinceId(Integer provinceId, Pageable pageable);

}
