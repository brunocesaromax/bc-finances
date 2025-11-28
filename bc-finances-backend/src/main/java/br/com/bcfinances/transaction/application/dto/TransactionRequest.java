package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dueDay;

    private LocalDate payday;

    @NotNull
    private BigDecimal value;

    private String observation;

    @NotNull
    private TransactionType type;

    @NotNull
    private Long categoryId;

    private List<String> tags = new ArrayList<>();

    private List<AttachmentRequest> attachments = new ArrayList<>();

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AttachmentRequest {
        private String name;
        private String originalName;
        private String contentType;
        private Long size;
    }
}
