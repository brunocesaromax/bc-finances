package br.com.bcfinances.domain.entities;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.domain.valueobjects.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private Long id;
    private String description;
    private LocalDate dueDay;
    private LocalDate payday;
    private BigDecimal value;
    private String observation;
    private TransactionType type;
    private Category category;
    private Person person;
    private String attachment;
    private String urlAttachment;

    public Transaction() {}

    public Transaction(String description, LocalDate dueDay, LocalDate payday, 
                      BigDecimal value, String observation, TransactionType type,
                      Category category, Person person, String attachment) {
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.category = category;
        this.person = person;
        this.attachment = attachment;
        this.validateTransaction();
    }

    public Transaction(String description, LocalDate dueDay, BigDecimal value, 
                      TransactionType type, Category category, Person person) {
        this(description, dueDay, null, value, null, type, category, person, null);
    }

    private void validateTransaction() {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (dueDay == null) {
            throw new IllegalArgumentException("Due day is required");
        }
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }
        if (person == null) {
            throw new IllegalArgumentException("Person is required");
        }
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
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        this.description = description;
    }

    public LocalDate getDueDay() {
        return dueDay;
    }

    public void setDueDay(LocalDate dueDay) {
        if (dueDay == null) {
            throw new IllegalArgumentException("Due day is required");
        }
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
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        this.value = value;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }
        this.category = category;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person is required");
        }
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
        return TransactionType.RECIPE.equals(this.type);
    }

    public boolean isExpense() {
        return TransactionType.EXPENSE.equals(this.type);
    }

    public boolean isOverdue() {
        return payday == null && dueDay.isBefore(LocalDate.now());
    }

    public boolean isPaid() {
        return payday != null;
    }

    public boolean hasAttachment() {
        return attachment != null && !attachment.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dueDay=" + dueDay +
                ", payday=" + payday +
                ", value=" + value +
                ", type=" + type +
                ", category=" + (category != null ? category.getName() : null) +
                ", person=" + (person != null ? person.getName() : null) +
                '}';
    }
}