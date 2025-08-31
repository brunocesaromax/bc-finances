package br.com.bcfinances.application.dto.transaction;

import br.com.bcfinances.category.application.dto.CategoryResponse;
import br.com.bcfinances.person.application.dto.PersonResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public TransactionResponse() {}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public PersonResponse getPerson() {
        return person;
    }

    public void setPerson(PersonResponse person) {
        this.person = person;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getUrlAttachment() {
        return urlAttachment;
    }

    public void setUrlAttachment(String urlAttachment) {
        this.urlAttachment = urlAttachment;
    }

    public boolean isRecipe() {
        return "RECIPE".equals(this.type);
    }

    public boolean isExpense() {
        return "EXPENSE".equals(this.type);
    }
}