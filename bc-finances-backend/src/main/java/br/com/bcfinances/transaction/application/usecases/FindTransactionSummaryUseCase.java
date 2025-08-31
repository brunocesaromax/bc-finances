package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.TransactionSummaryDto;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTransactionSummaryUseCase {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionSummaryDto> execute(TransactionFilterDto filter) {
        return transactionRepository.findSummary(filter);
    }

    @Transactional(readOnly = true)
    public List<TransactionSummaryDto> execute(TransactionFilterDto filter, int page, int size) {
        return transactionRepository.findSummaryPaged(filter, page, size);
    }

    @Transactional(readOnly = true)
    public long count(TransactionFilterDto filter) {
        return transactionRepository.countSummary(filter);
    }
}