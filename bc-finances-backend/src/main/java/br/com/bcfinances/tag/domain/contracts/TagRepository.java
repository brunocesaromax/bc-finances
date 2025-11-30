package br.com.bcfinances.tag.domain.contracts;

import br.com.bcfinances.tag.domain.entities.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {
    Tag save(Tag tag);
    List<Tag> saveAll(Collection<Tag> tags);
    Optional<Tag> findByNameIgnoreCase(String name);
    List<Tag> findAll();
    Set<Tag> findByNamesIgnoreCase(Collection<String> names);
}
