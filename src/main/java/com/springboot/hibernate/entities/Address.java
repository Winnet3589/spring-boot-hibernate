package com.springboot.hibernate.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.hibernate.entities.base.BaseEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Source;
import org.hibernate.annotations.SourceType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "ADDRESS")
public class Address extends BaseEntity<Long> {

  @Column(name = "CITY")
  private String city;

  @Column(name = "STATE")
  private String state;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "PIN_CODE")
  private Integer pinCode;

//  @OneToOne(cascade = CascadeType.ALL,fetch= FetchType.LAZY,mappedBy = "address")
  @JsonIgnoreProperties("citizenIDCard")
//  private Employee employee;

  @Version
  @Source(value = SourceType.DB)
  private Date version;
}
