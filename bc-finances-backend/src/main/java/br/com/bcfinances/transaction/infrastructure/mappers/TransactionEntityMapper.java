package br.com.bcfinances.transaction.infrastructure.mappers;

import br.com.bcfinances.category.infrastructure.mappers.CategoryEntityMapper;
import br.com.bcfinances.tag.infrastructure.mappers.TagEntityMapper;
import br.com.bcfinances.tag.infrastructure.persistence.TagEntity;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.domain.entities.TransactionAttachment;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import br.com.bcfinances.transaction.infrastructure.persistence.TransactionAttachmentEntity;
import br.com.bcfinances.transaction.infrastructure.persistence.TransactionEntity;
import br.com.bcfinances.transaction.infrastructure.persistence.TransactionTypeEntity;
import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionEntityMapper {

    private final CategoryEntityMapper categoryEntityMapper;
    private final TagEntityMapper tagEntityMapper;
    private final TransactionAttachmentEntityMapper attachmentEntityMapper;
    private final S3Service s3Service;

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
        entity.setDeletedAt(domain.getDeletedAt());

        Set<TagEntity> tagEntities = domain.getTags() != null
                ? domain.getTags().stream()
                .map(tagEntityMapper::toEntity)
                .collect(Collectors.toCollection(HashSet::new))
                : new HashSet<>();
        entity.setTags(tagEntities);

        List<TransactionAttachmentEntity> attachmentEntities = new ArrayList<>();
        if (domain.getAttachments() != null) {
            for (TransactionAttachment attachment : domain.getAttachments()) {
                TransactionAttachmentEntity attachmentEntity = attachmentEntityMapper.toEntity(attachment);
                attachmentEntity.setTransaction(entity);
                attachmentEntities.add(attachmentEntity);
            }
        }
        entity.setAttachments(attachmentEntities);

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
        domain.setDeletedAt(entity.getDeletedAt());

        List<TransactionAttachment> attachments = new ArrayList<>();
        if (entity.getAttachments() != null) {
            for (TransactionAttachmentEntity attachmentEntity : entity.getAttachments()) {
                TransactionAttachment attachment = attachmentEntityMapper.toDomain(attachmentEntity);
                attachment.setUrl(s3Service.configureUrl(attachment.getObjectKey()));
                attachments.add(attachment);
            }
        }
        domain.setAttachments(attachments);

        if (entity.getTags() != null) {
            domain.setTags(entity.getTags().stream()
                    .map(tagEntityMapper::toDomain)
                    .toList());
        }

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
