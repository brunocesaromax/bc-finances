package br.com.bcfinances.infrastructure.mappers;

import br.com.bcfinances.domain.entities.Category;
import br.com.bcfinances.infrastructure.persistence.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper {

    public CategoryEntity toEntity(Category domain) {
        if (domain == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());

        return entity;
    }

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        Category domain = new Category();
        domain.setId(entity.getId());
        domain.setName(entity.getName());

        return domain;
    }
}