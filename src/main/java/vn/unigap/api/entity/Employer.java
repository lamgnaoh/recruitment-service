package vn.unigap.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(schema = "tbl_employer")
public class Employer extends BaseEntity {
  @Column(name = "email",unique = true)
  private String email;

  @Lob
  @Column(name = "name", length = 256)
  private String name;

  @Column(name = "province")
  private String province;

  @Lob
  @Column(name = "description", length = 256)
  private String description;
}
