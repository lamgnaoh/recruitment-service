package vn.unigap.api.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.unigap.api.entity.Seeker;

public interface SeekerRepository extends JpaRepository<Seeker,Integer> {

  Page<Seeker> findAllByProvinceId(Integer provinceId, Pageable pageable);

  Integer countByCreateDateBetween(LocalDateTime start, LocalDateTime end);

//  @Query(value = "select * from seeker s "
//      + "inner join resume r on s.id = r.seeker_id "
//      + "where r.salary <= :salary "
//      + "and ("
//      + " :#{#fields.stream().map(f > 'r.fields like %-' + f + '-%').collect(Collectors.joining(' or '))}) "
//      + "and ("
//      + " :#{#provinces.stream().map(f > 'r.provinces like %-' + f + '-%').collect(Collectors.joining(' or '))}) " , nativeQuery = true)
//  List<Seeker> findByJobSalaryAndProvincesAndFields(
//      @Param("salary") BigDecimal salary,
//      @Param("provinces") List<Integer> provinceIds,
//      @Param("fields") List<Integer> fieldIds);
}
