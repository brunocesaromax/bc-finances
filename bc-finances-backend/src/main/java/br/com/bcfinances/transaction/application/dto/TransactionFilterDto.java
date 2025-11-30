package br.com.bcfinances.transaction.application.dto;

import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TransactionFilterDto {

    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDayStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDayEnd;
    private TransactionType type;
    private Long categoryId;
    private List<String> tags = new ArrayList<>();
}
