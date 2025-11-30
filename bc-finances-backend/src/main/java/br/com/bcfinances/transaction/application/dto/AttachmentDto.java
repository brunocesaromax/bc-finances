package br.com.bcfinances.transaction.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    
    private String name;
    private String originalName;
    private String contentType;
    private Long size;
    private String url;
}
