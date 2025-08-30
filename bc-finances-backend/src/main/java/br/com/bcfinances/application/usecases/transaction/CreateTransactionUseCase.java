package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.entities.Category;
import br.com.bcfinances.domain.entities.Person;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.domain.repositories.CategoryRepository;
import br.com.bcfinances.domain.repositories.PersonRepository;
import br.com.bcfinances.application.dto.transaction.TransactionRequest;
import br.com.bcfinances.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.application.mappers.TransactionMapper;
import br.com.bcfinances.domain.exceptions.PersonInactiveException;
import br.com.bcfinances.storage.S3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CreateTransactionUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private S3 s3;

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