package br.com.bcfinances.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CityJpaRepository extends JpaRepository<CityEntity, Long> {
    
    List<CityEntity> findByStateId(Long stateId);
}