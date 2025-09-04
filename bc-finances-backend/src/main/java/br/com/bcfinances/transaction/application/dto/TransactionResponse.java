package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.category.application.dto.CategoryResponse;
import br.com.bcfinances.person.application.dto.PersonResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private PersonResponse person;
    private String attachment;
    private String urlAttachment;

    public TransactionResponse(Long id, String description, LocalDate dueDay, LocalDate payday,
                             BigDecimal value, String observation, String type,
                             CategoryResponse category, PersonResponse person,
                             String attachment, String urlAttachment) {
        this.id = id;
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.category = category;
        this.person = person;
        this.attachment = attachment;
        this.urlAttachment = urlAttachment;
    }
}