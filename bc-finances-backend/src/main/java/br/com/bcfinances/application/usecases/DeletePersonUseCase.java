package br.com.bcfinances.application.usecases;

import br.com.bcfinances.domain.repositories.PersonRepository;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletePersonUseCase {

    private final PersonRepository personRepository;
    private final TransactionRepository transactionRepository;

    public DeletePersonUseCase(PersonRepository personRepository, TransactionRepository transactionRepository) {
        this.personRepository = personRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(Long id) {
        if (transactionRepository.existsByPersonId(id)) {
            throw new br.com.bcfinances.domain.exceptions.PersonExistentInTransactionException();
        }
        
        personRepository.deleteById(id);
    }
}