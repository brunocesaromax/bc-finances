package br.com.bcfinances.transaction.domain.entities;

import br.com.bcfinances.category.domain.entities.Category;
import br.com.bcfinances.tag.domain.entities.Tag;
import br.com.bcfinances.transaction.domain.valueobjects.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Transaction {

    @Setter
    private Long id;
    private String description;
    private LocalDate dueDay;
    @Setter
    private LocalDate payday;
    private BigDecimal value;
    @Setter
    private String observation;
    private TransactionType type;
    private Category category;
    private List<Tag> tags = new ArrayList<>();
    private List<TransactionAttachment> attachments = new ArrayList<>();
    @Setter
    private LocalDateTime deletedAt;

    public Transaction() {}

    public Transaction(String description, LocalDate dueDay, LocalDate payday,
                       BigDecimal value, String observation, TransactionType type,
                       Category category, List<Tag> tags, List<TransactionAttachment> attachments) {
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.category = category;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.attachments = attachments != null ? attachments : new ArrayList<>();
        this.validateTransaction();
    }

    public Transaction(String description, LocalDate dueDay, BigDecimal value,
                       TransactionType type, Category category) {
        this(description, dueDay, null, value, null, type, category, new ArrayList<>(), new ArrayList<>());
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
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        this.description = description;
    }

    public void setDueDay(LocalDate dueDay) {
        if (dueDay == null) {
            throw new IllegalArgumentException("Due day is required");
        }
        this.dueDay = dueDay;
    }

    public void setValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        this.value = value;
    }

    public void setType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        this.type = type;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }
        this.category = category;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void setAttachments(List<TransactionAttachment> attachments) {
        this.attachments = attachments != null ? attachments : new ArrayList<>();
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
                ", tags=" + (tags != null ? tags.size() : 0) +
                ", attachments=" + (attachments != null ? attachments.size() : 0) +
                '}';
    }
}
