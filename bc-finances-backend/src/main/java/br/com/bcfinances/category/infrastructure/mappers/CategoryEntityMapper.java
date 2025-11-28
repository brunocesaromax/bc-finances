package br.com.bcfinances.category.infrastructure.mappers;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.category.infrastructure.persistence.CategoryEntity;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import br.com.bcfinances.transaction.infrastructure.persistence.TransactionTypeEntity;
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
        entity.setTransactionType(mapTransactionType(domain.getTransactionType()));

        return entity;
    }

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        Category domain = new Category();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setTransactionType(mapTransactionType(entity.getTransactionType()));

        return domain;
    }

    private TransactionTypeEntity mapTransactionType(TransactionType domainType) {
        if (domainType == null) {
            return null;
        }
        return TransactionTypeEntity.valueOf(domainType.name());
    }

    private TransactionType mapTransactionType(TransactionTypeEntity entityType) {
        if (entityType == null) {
            return null;
        }
        return TransactionType.valueOf(entityType.name());
    }
}
