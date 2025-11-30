package br.com.bcfinances.tag.infrastructure.persistence;

import br.com.bcfinances.tag.domain.contracts.TagRepository;
import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.tag.infrastructure.mappers.TagEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository jpaRepository;
    private final TagEntityMapper mapper;

    @Override
    public Tag save(Tag tag) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tag)));
    }

    @Override
    public List<Tag> saveAll(Collection<Tag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return List.of();
        }

        return jpaRepository.saveAll(tags.stream()
                        .map(mapper::toEntity)
                        .toList())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Tag> findByNameIgnoreCase(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return jpaRepository.findByNameIgnoreCase(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<Tag> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Set<Tag> findByNamesIgnoreCase(Collection<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return Set.of();
        }

        Set<String> normalizedNames = names.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(name -> name.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        if (normalizedNames.isEmpty()) {
            return Set.of();
        }

        return jpaRepository.findByNameInIgnoreCase(normalizedNames).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }
}
