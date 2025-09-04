package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.auth.domain.contracts.UserRepository;
import br.com.bcfinances.auth.domain.entities.User;
import br.com.bcfinances.shared.infrastructure.mail.Mailer;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendOverdueTransactionAlertsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SendOverdueTransactionAlertsUseCase.class);
    private static final String RECIPIENTS_ROLE = "ROLE_SEARCH_TRANSACTION";

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final Mailer mailer;

    @Scheduled(cron = "0 0 6 * * *") // 6:00 AM daily
    @Transactional(readOnly = true)
    public void execute() {
        execute(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public void execute(LocalDate referenceDate) {
        logger.debug("Preparing to send overdue transaction alert emails.");

        List<Transaction> overdueTransactions = transactionRepository.findOverdueTransactions(referenceDate);

        if (overdueTransactions.isEmpty()) {
            logger.info("No overdue transactions to alert.");
            return;
        }

        logger.info("Found {} overdue transactions.", overdueTransactions.size());

        List<User> recipients = userRepository.findByPermissionsDescription(RECIPIENTS_ROLE);

        if (recipients.isEmpty()) {
            logger.warn("There are overdue transactions, but no recipients were found.");
            return;
        }

        mailer.alertOverdueTransactions(overdueTransactions, recipients);

        logger.info("Overdue transaction alert emails sent successfully.");
    }
}