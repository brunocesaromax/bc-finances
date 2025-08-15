package com.example.lancamentoapi.repository.listener;

import com.example.lancamentoapi.LancamentosApiApplication;
import com.example.lancamentoapi.model.Transaction;
import com.example.lancamentoapi.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class TransactionAttachmentListener {

    @PostLoad
    public void postLoad(Transaction transaction) {
        if (StringUtils.hasText(transaction.getAttachment())){
            S3 s3 = LancamentosApiApplication.getBean(S3.class);
            transaction.setUrlAttachment(s3.configureUrl(transaction.getAttachment()));
        }
    }
}