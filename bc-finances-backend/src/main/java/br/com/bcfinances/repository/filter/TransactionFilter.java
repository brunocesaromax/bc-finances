package br.com.bcfinances.repository.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TransactionFilter {

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDayStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDayEnd;
}
