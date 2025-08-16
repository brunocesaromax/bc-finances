package br.com.bcfinances.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cities")
@Setter
@Getter
public class City {

  @Id
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "state_id")
  private State state;
}
