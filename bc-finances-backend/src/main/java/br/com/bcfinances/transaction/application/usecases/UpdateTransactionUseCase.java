package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import br.com.bcfinances.person.domain.contracts.PersonRepository;
import br.com.bcfinances.person.domain.entities.Person;
import br.com.bcfinances.person.domain.exceptions.PersonInactiveException;
import br.com.bcfinances.transaction.application.dto.transaction.TransactionRequest;
import br.com.bcfinances.transaction.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.transaction.application.mappers.TransactionMapper;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UpdateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final PersonRepository personRepository;
    private final TransactionMapper transactionMapper;
    private final S3Service s3Service;

    @Transactional
    public TransactionResponse execute(Long id, TransactionRequest request) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Person person = validatePerson(request.getPersonId());

        // Validar pessoa apenas se mudou
        if (!person.getId().equals(existingTransaction.getPerson().getId())) {
            validatePerson(request.getPersonId());
        }

        // Gerenciar anexos S3
        String oldAttachment = existingTransaction.getAttachment();
        String newAttachment = request.getAttachment();

        if (!StringUtils.hasText(newAttachment) && StringUtils.hasText(oldAttachment)) {
            s3Service.delete(oldAttachment);
        } else if (StringUtils.hasText(newAttachment) && !newAttachment.equals(oldAttachment)) {
            s3Service.update(oldAttachment, newAttachment);
        }

        transactionMapper.updateEntity(existingTransaction, request, category, person);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);

        return transactionMapper.toResponse(updatedTransaction);
    }

    private Person validatePerson(Long personId) {
        if (personId == null) {
            throw new PersonInactiveException("Person ID cannot be null");
        }

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonInactiveException("Person not found"));

        if (Boolean.TRUE.equals(person.isInactive())) {
            throw new PersonInactiveException("Person is inactive");
        }

        return person;
    }
}