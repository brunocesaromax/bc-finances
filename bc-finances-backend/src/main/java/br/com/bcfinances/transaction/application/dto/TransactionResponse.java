package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.category.application.dto.CategoryResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class TransactionResponse {

    private Long id;
    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String observation;
    private String type;
    private CategoryResponse category;
    private List<String> tags;
    private List<AttachmentDto> attachments;

    public TransactionResponse(Long id, String description, LocalDate dueDay, LocalDate payday,
                               BigDecimal value, String observation, String type,
                               CategoryResponse category, List<String> tags,
                               List<AttachmentDto> attachments) {
        this.id = id;
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.category = category;
        this.tags = tags;
        this.attachments = attachments;
    }
}
