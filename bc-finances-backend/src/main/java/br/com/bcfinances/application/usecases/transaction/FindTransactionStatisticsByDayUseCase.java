package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.TransactionStatisticByDayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FindTransactionStatisticsByDayUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionStatisticByDayDto> execute(LocalDate monthReference) {
        return transactionRepository.findStatisticsByDay(monthReference);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticByDayDto> execute() {
        return execute(LocalDate.now());
    }
}