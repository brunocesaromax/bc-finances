package br.com.bcfinances.transaction.infrastructure.persistence;

import br.com.bcfinances.category.infrastructure.persistence.CategoryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@EntityListeners(TransactionAttachmentListener.class)
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String description;

    @NotNull
    @Column(name = "due_day")
    private LocalDate dueDay;

    @Column(name = "payday")
    private LocalDate payday;

    @NotNull
    private BigDecimal value;

    private String observation;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionTypeEntity type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private br.com.bcfinances.person.infrastructure.persistence.PersonEntity person;

    private String attachment;

    @Transient
    private String urlAttachment;

    public TransactionEntity() {}

    public TransactionEntity(String description, LocalDate dueDay, LocalDate payday,
                           BigDecimal value, String observation, TransactionTypeEntity type,
                           CategoryEntity category, br.com.bcfinances.person.infrastructure.persistence.PersonEntity person, String attachment) {
        this.description = description;
        this.dueDay = dueDay;
        this.payday = payday;
        this.value = value;
        this.observation = observation;
        this.type = type;
        this.category = category;
        this.person = person;
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dueDay=" + dueDay +
                ", payday=" + payday +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}