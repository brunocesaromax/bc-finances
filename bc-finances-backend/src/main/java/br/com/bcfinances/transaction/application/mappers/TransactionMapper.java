package br.com.bcfinances.transaction.application.mappers;

import br.com.bcfinances.category.application.mappers.CategoryMapper;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.transaction.application.dto.TransactionRequest;
import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CategoryMapper categoryMapper;
    private final br.com.bcfinances.person.application.mappers.PersonMapper personMapper;

    public Transaction toEntity(TransactionRequest request, Category category, Person person) {
        if (request == null) {
            return null;
        }

        return new Transaction(
                request.getDescription(),
                request.getDueDay(),
                request.getPayday(),
                request.getValue(),
                request.getObservation(),
                TransactionType.valueOf(request.getType()),
                category,
                person,
                request.getAttachment()
        );
    }

    public TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getDueDay(),
                transaction.getPayday(),
                transaction.getValue(),
                transaction.getObservation(),
                transaction.getType().name(),
                categoryMapper.toResponse(transaction.getCategory()),
                personMapper.toResponse(transaction.getPerson()),
                transaction.getAttachment(),
                transaction.getUrlAttachment()
        );
    }

    public void updateEntity(Transaction entity, TransactionRequest request, Category category, Person person) {
        if (entity == null || request == null) {
            return;
        }

        entity.setDescription(request.getDescription());
        entity.setDueDay(request.getDueDay());
        entity.setPayday(request.getPayday());
        entity.setValue(request.getValue());
        entity.setObservation(request.getObservation());
        entity.setType(TransactionType.valueOf(request.getType()));
        entity.setCategory(category);
        entity.setPerson(person);
        entity.setAttachment(request.getAttachment());
    }
}