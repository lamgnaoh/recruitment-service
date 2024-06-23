package vn.unigap.api.repository;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {
  @Nonnull
  @Cacheable(value = "province", key = "#id")
  Optional<Province> findById(@Nonnull Integer id);

  List<Province> findByIdIn(List<Integer> list);
}
