package br.com.bcfinances.tag.infrastructure.mappers;

import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.tag.infrastructure.persistence.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagEntityMapper {

    public TagEntity toEntity(Tag domain) {
        if (domain == null) {
            return null;
        }

        TagEntity entity = new TagEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }

    public Tag toDomain(TagEntity entity) {
        if (entity == null) {
            return null;
        }

        Tag domain = new Tag();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        return domain;
    }
}
