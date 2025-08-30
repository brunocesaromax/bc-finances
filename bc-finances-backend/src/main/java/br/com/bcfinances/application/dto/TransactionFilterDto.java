package br.com.bcfinances.application.dto;

import java.time.LocalDate;

public class TransactionFilterDto {

    private String description;
    private LocalDate dueDayFrom;
    private LocalDate dueDayTo;
    private String personName;
    private String categoryName;

    public TransactionFilterDto() {}

    public TransactionFilterDto(String description, LocalDate dueDayFrom, LocalDate dueDayTo, 
                              String personName, String categoryName) {
        this.description = description;
        this.dueDayFrom = dueDayFrom;
        this.dueDayTo = dueDayTo;
        this.personName = personName;
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDayFrom() {
        return dueDayFrom;
    }

    public void setDueDayFrom(LocalDate dueDayFrom) {
        this.dueDayFrom = dueDayFrom;
    }

    public LocalDate getDueDayTo() {
        return dueDayTo;
    }

    public void setDueDayTo(LocalDate dueDayTo) {
        this.dueDayTo = dueDayTo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}