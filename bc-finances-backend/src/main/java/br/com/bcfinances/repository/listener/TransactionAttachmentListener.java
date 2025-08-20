package br.com.bcfinances.repository.listener;

import br.com.bcfinances.BcFinancesApplication;
import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.storage.S3;
import org.springframework.util.StringUtils;

import jakarta.persistence.PostLoad;

public class TransactionAttachmentListener {

    @PostLoad
    public void postLoad(Transaction transaction) {
        if (StringUtils.hasText(transaction.getAttachment())){
            S3 s3 = BcFinancesApplication.getBean(S3.class);
            transaction.setUrlAttachment(s3.configureUrl(transaction.getAttachment()));
        }
    }
}