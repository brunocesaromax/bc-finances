package br.com.bcfinances.model;

import br.com.bcfinances.repository.listener.TransactionAttachmentListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


//Quando um objeto Transaction for carregado do banco o listener será ativado
@EntityListeners(TransactionAttachmentListener.class)
@Entity
@Table(name = "transactions")
@Data
public class Transaction {

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
    @Enumerated(EnumType.STRING) // Melhor para consultas escritas a mao
    private TypeTransaction type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //Não é necessários buscar os contatos de uma pessoa pela busca de lançamentos
    @JsonIgnoreProperties(Person_.CONTACTS)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private String attachment;

    @Transient
    private String urlAttachment;

    @JsonIgnore
    public boolean isRecipe(){
        return this.type.equals(TypeTransaction.RECIPE);
    }
}
