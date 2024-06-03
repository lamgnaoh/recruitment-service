package vn.unigap.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {

  List<Province> findByIdIn(List<Integer> list);
}
