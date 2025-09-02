package br.com.bcfinances.person.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonJpaRepository extends JpaRepository<PersonEntity, Long> {

    @Query("select p from PersonEntity p " +
            "where (:name is null or lower(p.name) like lower(concat('%', cast(:name as string), '%')) )")
    Page<PersonEntity> findAllByName(String name, Pageable pageable);
}