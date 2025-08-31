package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.category.domain.contracts.CategoryRepository;
import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.infrastructure.storage.S3;
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
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final PersonRepository personRepository;
    private final TransactionMapper transactionMapper;
    private final S3 s3;

    @Transactional
    public TransactionResponse execute(TransactionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Person person = validatePerson(request.getPersonId());

        Transaction transaction = transactionMapper.toEntity(request, category, person);

        if (StringUtils.hasText(transaction.getAttachment())) {
            s3.save(transaction.getAttachment());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
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