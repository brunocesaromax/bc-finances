package br.com.bcfinances.application.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {

    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String observation;
    private String type;
    private Long categoryId;
    private Long personId;
    private String attachment;

    public TransactionRequest() {}

    public TransactionRequest(String description, LocalDate dueDay, LocalDate payday,
                            BigDecimal value, String observation, String type,
                            Long categoryId, Long personId, String attachment) {
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.categoryId = categoryId;
        this.personId = personId;
        this.attachment = attachment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDay() {
        return dueDay;
    }

    public void setDueDay(LocalDate dueDay) {
        this.dueDay = dueDay;
    }

    public LocalDate getPayday() {
        return payday;
    }

    public void setPayday(LocalDate payday) {
        this.payday = payday;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}