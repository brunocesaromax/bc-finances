package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionStatisticCategoryDto;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTransactionStatisticsByCategoryUseCase {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionStatisticCategoryDto> execute(LocalDate monthReference) {
        return transactionRepository.findStatisticsByCategory(monthReference);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticCategoryDto> execute() {
        return execute(LocalDate.now());
    }
}