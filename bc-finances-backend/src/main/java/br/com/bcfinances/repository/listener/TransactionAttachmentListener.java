package br.com.bcfinances.repository.listener;

import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.storage.S3;
import jakarta.persistence.PostLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionAttachmentListener {

    private final S3 s3;

    @PostLoad
    public void postLoad(Transaction transaction) {
        if (StringUtils.hasText(transaction.getAttachment())){
            transaction.setUrlAttachment(s3.configureUrl(transaction.getAttachment()));
        }
    }
}