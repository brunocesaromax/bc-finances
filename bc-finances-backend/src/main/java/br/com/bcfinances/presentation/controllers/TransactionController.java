package br.com.bcfinances.presentation.controllers;

import br.com.bcfinances.application.dto.TransactionFilterDto;
import br.com.bcfinances.application.dto.TransactionStatisticByDayDto;
import br.com.bcfinances.application.dto.TransactionStatisticCategoryDto;
import br.com.bcfinances.application.dto.TransactionSummaryDto;
import br.com.bcfinances.application.dto.transaction.TransactionRequest;
import br.com.bcfinances.application.dto.transaction.TransactionResponse;
import br.com.bcfinances.application.usecases.transaction.CreateTransactionUseCase;
import br.com.bcfinances.application.usecases.transaction.DeleteTransactionUseCase;
import br.com.bcfinances.application.usecases.transaction.FindOverdueTransactionsUseCase;
import br.com.bcfinances.application.usecases.transaction.FindTransactionByIdUseCase;
import br.com.bcfinances.application.usecases.transaction.FindTransactionStatisticsByCategoryUseCase;
import br.com.bcfinances.application.usecases.transaction.FindTransactionStatisticsByDayUseCase;
import br.com.bcfinances.application.usecases.transaction.FindTransactionSummaryUseCase;
import br.com.bcfinances.application.usecases.transaction.FindTransactionsUseCase;
import br.com.bcfinances.application.usecases.transaction.GenerateTransactionReportByPersonUseCase;
import br.com.bcfinances.application.usecases.transaction.UpdateTransactionUseCase;
import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.person.domain.exceptions.PersonInactiveException;
import br.com.bcfinances.infrastructure.persistence.Attachment;
import br.com.bcfinances.infrastructure.storage.S3;
import org.springframework.context.ApplicationEventPublisher;
import br.com.bcfinances.infrastructure.event.ResourceCreatedEvent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private CreateTransactionUseCase createTransactionUseCase;

    @Autowired
    private FindTransactionByIdUseCase findTransactionByIdUseCase;

    @Autowired
    private UpdateTransactionUseCase updateTransactionUseCase;

    @Autowired
    private DeleteTransactionUseCase deleteTransactionUseCase;

    @Autowired
    private FindTransactionsUseCase findTransactionsUseCase;

    @Autowired
    private FindTransactionSummaryUseCase findTransactionSummaryUseCase;

    @Autowired
    private FindTransactionStatisticsByCategoryUseCase findStatisticsByCategoryUseCase;

    @Autowired
    private FindTransactionStatisticsByDayUseCase findStatisticsByDayUseCase;

    @Autowired
    private FindOverdueTransactionsUseCase findOverdueTransactionsUseCase;

    @Autowired
    private GenerateTransactionReportByPersonUseCase generateReportByPersonUseCase;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private S3 s3;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public ResponseEntity<TransactionResponse> findById(@PathVariable Long id) {
        Optional<TransactionResponse> transaction = findTransactionByIdUseCase.execute(id);
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request, 
                                                      HttpServletResponse response) {
        TransactionResponse transactionResponse = createTransactionUseCase.execute(request);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, transactionResponse.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody TransactionRequest request) {
        try {
            TransactionResponse response = updateTransactionUseCase.execute(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVE_TRANSACTION')")
    public void delete(@PathVariable Long id) {
        deleteTransactionUseCase.execute(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public Page<TransactionResponse> list(@ModelAttribute TransactionFilterDto filter, Pageable pageable) {
        List<TransactionResponse> transactions = findTransactionsUseCase.execute(filter, pageable.getPageNumber(), pageable.getPageSize());
        long totalElements = findTransactionsUseCase.count(filter);
        return new PageImpl<>(transactions, pageable, totalElements);
    }

    @GetMapping(params = "summary")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public Page<TransactionSummaryDto> summary(@ModelAttribute TransactionFilterDto filter, Pageable pageable) {
        return transactionRepository.findWithFilter(filter, pageable);
    }

    @GetMapping("/statistics/category")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public List<TransactionStatisticCategoryDto> findStatisticsByCategory() {
        return findStatisticsByCategoryUseCase.execute();
    }

    @GetMapping("/statistics/day")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public List<TransactionStatisticByDayDto> findStatisticsByDay() {
        return findStatisticsByDayUseCase.execute();
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public List<TransactionResponse> findOverdueTransactions() {
        return findOverdueTransactionsUseCase.execute();
    }

    @GetMapping("/reports/person")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public ResponseEntity<byte[]> reportByPerson(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws JRException {
        byte[] report = generateReportByPersonUseCase.execute(start, end);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(report);
    }

    @PostMapping("/attachment")
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public Attachment uploadAttachment(@RequestParam MultipartFile attachment) throws IOException {
        String name = s3.saveTemp(attachment);
        return new Attachment(name, s3.configureUrl(name));
    }

    @ExceptionHandler({PersonInactiveException.class})
    public ResponseEntity<Object> handlePersonInactiveException(PersonInactiveException ex) {
        String msgUser = messageSource.getMessage("person.not-exists-or-inactive", null, LocaleContextHolder.getLocale());
        String msgDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

        // Simple error structure for now - can be enhanced later
        var error = new ErrorResponse(msgUser, msgDev);
        return ResponseEntity.badRequest().body(Collections.singletonList(error));
    }

    // Simple error response class - can be moved to a common package later
    private record ErrorResponse(String userMessage, String devMessage) {
    }
}