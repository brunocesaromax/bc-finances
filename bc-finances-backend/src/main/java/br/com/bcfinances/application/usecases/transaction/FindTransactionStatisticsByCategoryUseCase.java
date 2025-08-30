package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.TransactionStatisticCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FindTransactionStatisticsByCategoryUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionStatisticCategoryDto> execute(LocalDate monthReference) {
        return transactionRepository.findStatisticsByCategory(monthReference);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticCategoryDto> execute() {
        return execute(LocalDate.now());
    }
}