package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.infrastructure.storage.S3;
import jakarta.persistence.PostLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionAttachmentListener {

    private final S3 s3;

    @PostLoad
    public void postLoad(TransactionEntity transactionEntity) {
        if (StringUtils.hasText(transactionEntity.getAttachment())){
            transactionEntity.setUrlAttachment(s3.configureUrl(transactionEntity.getAttachment()));
        }
    }
}