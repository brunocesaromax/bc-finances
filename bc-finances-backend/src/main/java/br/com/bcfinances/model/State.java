package br.com.bcfinances.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "states")
@Setter
@Getter
public class State {

  @Id
  //Não é necessário definir a estratégia de geração de chave primária, pois não haverá um resource para States, somente consulta
  private Long id;

  private String name;
}
