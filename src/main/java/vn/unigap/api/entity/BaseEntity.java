package vn.unigap.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @CreatedDate
  @Column(name = "created_at")
  protected LocalDateTime createDate;

  @LastModifiedDate
  @Column(name = "updated_at")
  protected LocalDateTime modifiedDate;

}
