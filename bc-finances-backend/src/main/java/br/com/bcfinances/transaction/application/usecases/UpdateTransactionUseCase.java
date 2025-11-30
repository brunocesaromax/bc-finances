package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import br.com.bcfinances.tag.domain.contracts.TagRepository;
import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.transaction.application.dto.TransactionRequest;
import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.application.mappers.TransactionMapper;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.domain.entities.TransactionAttachment;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TransactionMapper transactionMapper;
    private final S3Service s3Service;

    @Transactional
    public TransactionResponse execute(Long id, TransactionRequest request) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        validateCategoryType(category, request.getType());
        validateTagNames(request.getTags());

        List<Tag> tags = resolveTags(request.getTags());
        List<TransactionAttachment> requestedAttachments = buildAttachments(request);

        Map<String, TransactionAttachment> existingAttachmentsByKey = mapByObjectKey(existingTransaction.getAttachments());
        Set<String> requestedKeys = requestedAttachments.stream()
                .map(TransactionAttachment::getObjectKey)
                .filter(StringUtils::hasText)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        // Preserve ids for existing attachments
        requestedAttachments.forEach(attachment -> {
            TransactionAttachment existing = existingAttachmentsByKey.get(attachment.getObjectKey());
            if (existing != null) {
                attachment.setId(existing.getId());
            }
        });

        // Delete removed attachments
        existingAttachmentsByKey.keySet().stream()
                .filter(key -> !requestedKeys.contains(key))
                .forEach(s3Service::delete);

        // Save new attachments
        requestedKeys.stream()
                .filter(StringUtils::hasText)
                .filter(key -> !existingAttachmentsByKey.containsKey(key))
                .forEach(s3Service::save);

        transactionMapper.updateEntity(existingTransaction, request, category, tags, requestedAttachments);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);

        return transactionMapper.toResponse(updatedTransaction);
    }

    private void validateCategoryType(Category category, TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (category.getTransactionType() != type) {
            throw new IllegalArgumentException("Category type does not match transaction type");
        }
    }

    private void validateTagNames(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        boolean hasInvalid = tagNames.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .anyMatch(name -> name.length() < 3);

        if (hasInvalid) {
            throw new IllegalArgumentException("Tags must have at least 3 characters");
        }
    }

    private List<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }

        LinkedHashSet<String> normalizedNames = tagNames.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(name -> name.length() > 80 ? name.substring(0, 80) : name)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        if (normalizedNames.isEmpty()) {
            return List.of();
        }

        Map<String, Tag> existingByLower = new LinkedHashMap<>();
        tagRepository.findByNamesIgnoreCase(normalizedNames)
                .forEach(tag -> existingByLower.put(tag.getName().toLowerCase(Locale.ROOT), tag));

        List<Tag> result = new ArrayList<>(existingByLower.values());

        List<Tag> newTags = normalizedNames.stream()
                .filter(name -> !existingByLower.containsKey(name.toLowerCase(Locale.ROOT)))
                .map(Tag::new)
                .toList();

        if (!newTags.isEmpty()) {
            result.addAll(tagRepository.saveAll(newTags));
        }

        return result;
    }

    private List<TransactionAttachment> buildAttachments(TransactionRequest request) {
        if (request.getAttachments() == null) {
            return List.of();
        }

        return request.getAttachments().stream()
                .filter(att -> att != null && StringUtils.hasText(att.getName()))
                .map(att -> {
                    TransactionAttachment attachment = new TransactionAttachment(
                            att.getName(),
                            att.getOriginalName(),
                            att.getContentType(),
                            att.getSize()
                    );
                    attachment.setUrl(s3Service.configureUrl(att.getName()));
                    return attachment;
                })
                .toList();
    }

    private Map<String, TransactionAttachment> mapByObjectKey(List<TransactionAttachment> attachments) {
        Map<String, TransactionAttachment> result = new LinkedHashMap<>();
        if (attachments == null) {
            return result;
        }

        attachments.stream()
                .filter(att -> att != null && StringUtils.hasText(att.getObjectKey()))
                .forEach(att -> result.put(att.getObjectKey(), att));
        return result;
    }
}
