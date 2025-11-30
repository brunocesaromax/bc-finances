package br.com.bcfinances.transaction.application.mappers;

import br.com.bcfinances.category.application.mappers.CategoryMapper;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.transaction.application.dto.AttachmentDto;
import br.com.bcfinances.transaction.application.dto.TransactionRequest;
import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.domain.entities.TransactionAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CategoryMapper categoryMapper;

    public Transaction toEntity(TransactionRequest request, Category category, List<Tag> tags, List<TransactionAttachment> attachments) {
        if (request == null) {
            return null;
        }

        return new Transaction(
                request.getDescription(),
                request.getDueDay(),
                request.getPayday(),
                request.getValue(),
                request.getObservation(),
                request.getType(),
                category,
                tags,
                attachments
        );
    }

    public TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        List<String> tagNames = transaction.getTags() != null
                ? transaction.getTags().stream()
                .map(Tag::getName)
                .filter(name -> name != null && !name.isBlank())
                .toList()
                : List.of();

        List<AttachmentDto> attachmentDtos = transaction.getAttachments() != null
                ? transaction.getAttachments().stream()
                .map(this::toAttachmentDto)
                .toList()
                : List.of();

        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getDueDay(),
                transaction.getPayday(),
                transaction.getValue(),
                transaction.getObservation(),
                transaction.getType().name(),
                categoryMapper.toResponse(transaction.getCategory()),
                tagNames,
                attachmentDtos
        );
    }

    public void updateEntity(Transaction entity, TransactionRequest request, Category category, List<Tag> tags, List<TransactionAttachment> attachments) {
        if (entity == null || request == null) {
            return;
        }

        entity.setDescription(request.getDescription());
        entity.setDueDay(request.getDueDay());
        entity.setPayday(request.getPayday());
        entity.setValue(request.getValue());
        entity.setObservation(request.getObservation());
        entity.setType(request.getType());
        entity.setCategory(category);
        entity.setTags(tags != null ? new ArrayList<>(tags) : new ArrayList<>());
        entity.setAttachments(attachments != null ? new ArrayList<>(attachments) : new ArrayList<>());
    }

    private AttachmentDto toAttachmentDto(TransactionAttachment attachment) {
        return new AttachmentDto(
                attachment.getObjectKey(),
                attachment.getOriginalName(),
                attachment.getContentType(),
                attachment.getSize(),
                attachment.getUrl()
        );
    }
}
