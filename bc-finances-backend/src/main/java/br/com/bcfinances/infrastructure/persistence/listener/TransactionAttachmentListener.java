package br.com.bcfinances.infrastructure.persistence.listener;

import br.com.bcfinances.infrastructure.persistence.TransactionEntity;
import br.com.bcfinances.storage.S3;
import jakarta.persistence.PostLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TransactionAttachmentListener {

    @Autowired
    private S3 s3;

    @PostLoad
    public void postLoad(TransactionEntity transactionEntity) {
        if (StringUtils.hasText(transactionEntity.getAttachment())){
            transactionEntity.setUrlAttachment(s3.configureUrl(transactionEntity.getAttachment()));
        }
    }
}