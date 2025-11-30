package br.com.bcfinances.transaction.application.usecases;

import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import br.com.bcfinances.transaction.domain.entities.Transaction;
import br.com.bcfinances.transaction.domain.entities.TransactionAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final S3Service s3Service;

    @Transactional
    public void execute(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (transaction.getAttachments() != null) {
            transaction.getAttachments().stream()
                    .map(TransactionAttachment::getObjectKey)
                    .filter(StringUtils::hasText)
                    .forEach(s3Service::delete);
        }

        transactionRepository.deleteById(id);
    }
}
