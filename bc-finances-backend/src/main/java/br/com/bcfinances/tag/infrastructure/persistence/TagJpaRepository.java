package br.com.bcfinances.tag.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByNameIgnoreCase(String name);

    @Query("select t from TagEntity t where lower(t.name) in :names")
    List<TagEntity> findByNameInIgnoreCase(@Param("names") Collection<String> names);
}
