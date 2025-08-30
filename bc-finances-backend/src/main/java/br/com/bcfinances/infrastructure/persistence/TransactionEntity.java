package br.com.bcfinances.infrastructure.persistence;

import br.com.bcfinances.infrastructure.persistence.listener.TransactionAttachmentListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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
    private PersonEntity person;

    private String attachment;

    @Transient
    private String urlAttachment;

    public TransactionEntity() {}

    public TransactionEntity(String description, LocalDate dueDay, LocalDate payday,
                           BigDecimal value, String observation, TransactionTypeEntity type,
                           CategoryEntity category, PersonEntity person, String attachment) {
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

    public TransactionTypeEntity getType() {
        return type;
    }

    public void setType(TransactionTypeEntity type) {
        this.type = type;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
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
        return TransactionTypeEntity.RECIPE.equals(this.type);
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