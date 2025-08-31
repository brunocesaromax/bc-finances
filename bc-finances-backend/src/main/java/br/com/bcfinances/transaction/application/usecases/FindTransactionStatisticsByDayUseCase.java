package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.transaction.application.dto.TransactionStatisticByDayDto;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTransactionStatisticsByDayUseCase {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionStatisticByDayDto> execute(LocalDate monthReference) {
        return transactionRepository.findStatisticsByDay(monthReference);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticByDayDto> execute() {
        return execute(LocalDate.now());
    }
}