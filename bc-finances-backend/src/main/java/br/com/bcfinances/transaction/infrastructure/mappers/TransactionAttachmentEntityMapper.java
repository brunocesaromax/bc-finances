package br.com.bcfinances.transaction.infrastructure.mappers;

import br.com.bcfinances.transaction.domain.entities.TransactionAttachment;
import br.com.bcfinances.transaction.infrastructure.persistence.TransactionAttachmentEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionAttachmentEntityMapper {

    public TransactionAttachmentEntity toEntity(TransactionAttachment domain) {
        if (domain == null) {
            return null;
        }

        TransactionAttachmentEntity entity = new TransactionAttachmentEntity();
        entity.setId(domain.getId());
        entity.setObjectKey(domain.getObjectKey());
        entity.setOriginalName(domain.getOriginalName());
        entity.setContentType(domain.getContentType());
        entity.setSize(domain.getSize());
        entity.setUrl(domain.getUrl());
        return entity;
    }

    public TransactionAttachment toDomain(TransactionAttachmentEntity entity) {
        if (entity == null) {
            return null;
        }

        TransactionAttachment attachment = new TransactionAttachment();
        attachment.setId(entity.getId());
        attachment.setObjectKey(entity.getObjectKey());
        attachment.setOriginalName(entity.getOriginalName());
        attachment.setContentType(entity.getContentType());
        attachment.setSize(entity.getSize());
        attachment.setUrl(entity.getUrl());
        return attachment;
    }
}
