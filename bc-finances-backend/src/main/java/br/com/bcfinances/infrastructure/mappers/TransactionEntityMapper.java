package br.com.bcfinances.infrastructure.mappers;

import br.com.bcfinances.category.infrastructure.mappers.CategoryEntityMapper;
import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.valueobjects.TransactionType;
import br.com.bcfinances.infrastructure.persistence.TransactionEntity;
import br.com.bcfinances.infrastructure.persistence.TransactionTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityMapper {

    @Autowired
    private CategoryEntityMapper categoryEntityMapper;

    @Autowired
    private PersonEntityMapper personEntityMapper;

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setDescription(domain.getDescription());
        entity.setDueDay(domain.getDueDay());
        entity.setPayday(domain.getPayday());
        entity.setValue(domain.getValue());
        entity.setObservation(domain.getObservation());
        entity.setType(mapTransactionType(domain.getType()));
        entity.setCategory(categoryEntityMapper.toEntity(domain.getCategory()));
        entity.setPerson(personEntityMapper.toEntity(domain.getPerson()));
        entity.setAttachment(domain.getAttachment());
        entity.setUrlAttachment(domain.getUrlAttachment());

        return entity;
    }

    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        Transaction domain = new Transaction();
        domain.setId(entity.getId());
        domain.setDescription(entity.getDescription());
        domain.setDueDay(entity.getDueDay());
        domain.setPayday(entity.getPayday());
        domain.setValue(entity.getValue());
        domain.setObservation(entity.getObservation());
        domain.setType(mapTransactionType(entity.getType()));
        domain.setCategory(categoryEntityMapper.toDomain(entity.getCategory()));
        domain.setPerson(personEntityMapper.toDomain(entity.getPerson()));
        domain.setAttachment(entity.getAttachment());
        domain.setUrlAttachment(entity.getUrlAttachment());

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