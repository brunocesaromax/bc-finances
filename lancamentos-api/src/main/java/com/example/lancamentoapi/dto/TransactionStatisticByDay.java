package com.example.lancamentoapi.dto;

import com.example.lancamentoapi.model.TypeLaunch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticByDay {

    private TypeLaunch type;
    private LocalDate day;
    private BigDecimal total;
}
