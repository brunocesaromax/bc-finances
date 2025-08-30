package br.com.bcfinances.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonJpaRepository extends JpaRepository<PersonEntity, Long> {

    @Query("select p from PersonEntity p " +
            "where (:name is null or p.name like concat('%', :name, '%') )")
    Page<PersonEntity> findAllByName(String name, Pageable pageable);
}