package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.TransactionFilterDto;
import br.com.bcfinances.application.dto.TransactionSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindTransactionSummaryUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

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