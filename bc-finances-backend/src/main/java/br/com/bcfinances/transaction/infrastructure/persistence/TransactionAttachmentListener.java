package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import jakarta.persistence.PostLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionAttachmentListener {

    private final S3Service s3Service;

    @PostLoad
    public void postLoad(TransactionEntity transactionEntity) {
        if (StringUtils.hasText(transactionEntity.getAttachment())){
            transactionEntity.setUrlAttachment(s3Service.configureUrl(transactionEntity.getAttachment()));
        }
    }
}