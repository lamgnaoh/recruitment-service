package vn.unigap.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "employer")
@Getter
@Setter
public class Employer extends BaseEntity {
  @Column(name = "email",unique = true)
  private String email;

  @Lob
  @Column(name = "name", length = 256)
  private String name;

  @Column(name = "province")
  private Integer province;

  @Lob
  @Column(name = "description", length = 256)
  private String description;
}
