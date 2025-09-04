package br.com.bcfinances.transaction.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String observation;
    private String type;
    private CategoryRequest category;
    private PersonRequest person;
    private String attachment;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PersonRequest {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryRequest {
        private Long id;
        private String name;
    }
}