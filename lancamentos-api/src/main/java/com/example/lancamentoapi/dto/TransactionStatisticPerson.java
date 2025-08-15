package com.example.lancamentoapi.dto;

import com.example.lancamentoapi.model.Person;
import com.example.lancamentoapi.model.TypeLaunch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class TransactionStatisticPerson {

    private TypeLaunch type;
    private Person person;
    private BigDecimal total;
}
