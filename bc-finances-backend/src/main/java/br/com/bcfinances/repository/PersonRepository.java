package br.com.bcfinances.repository;

import br.com.bcfinances.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long>{

    @Query("select p from Person p " +
            "where (:name is null or p.name like concat('%', :name, '%') )")
    Page<Person> findAllByName(String name, Pageable pageable);
}
