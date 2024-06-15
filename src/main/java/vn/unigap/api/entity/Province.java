package vn.unigap.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "job_province")
@Getter
@Setter
public class Province {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;
  private String slug;
}
