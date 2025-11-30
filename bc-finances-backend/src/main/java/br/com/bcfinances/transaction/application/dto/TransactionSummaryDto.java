package br.com.bcfinances.transaction.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class TransactionSummaryDto {

    private Long id;
    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String type;
    private String categoryName;
    private List<String> tags;
    private boolean hasAttachments;

    public TransactionSummaryDto(Long id, String description, LocalDate dueDay, LocalDate payday,
                                 BigDecimal value, String type, String categoryName,
                                 List<String> tags, boolean hasAttachments) {
        this.id = id;
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.type = type;
        this.categoryName = categoryName;
        this.tags = tags;
        this.hasAttachments = hasAttachments;
    }
}
