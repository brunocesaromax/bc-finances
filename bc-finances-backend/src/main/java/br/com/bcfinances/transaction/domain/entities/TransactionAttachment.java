package br.com.bcfinances.transaction.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAttachment {
    private Long id;
    private String objectKey;
    private String originalName;
    private String contentType;
    private Long size;
    private String url;

    public TransactionAttachment(String objectKey, String originalName, String contentType, Long size) {
        this.objectKey = objectKey;
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
    }
}
