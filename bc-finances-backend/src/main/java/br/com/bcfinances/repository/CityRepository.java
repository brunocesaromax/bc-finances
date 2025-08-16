package br.com.bcfinances.repository;

import br.com.bcfinances.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>{

    List<City> findByStateId(Long stateId);
}
