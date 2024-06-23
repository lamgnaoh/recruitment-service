package vn.unigap.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employer extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

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

  @OneToMany(mappedBy = "employer")
  private List<Job> jobs = new ArrayList<>();

  @ManyToMany
  private List<Province> provinces = new ArrayList<>();
}
