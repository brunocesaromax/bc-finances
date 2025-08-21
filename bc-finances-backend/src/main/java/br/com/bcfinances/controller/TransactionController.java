package br.com.bcfinances.controller;

import br.com.bcfinances.dto.Attachment;
import br.com.bcfinances.dto.TransactionStatisticByDay;
import br.com.bcfinances.dto.TransactionStatisticCategory;
import br.com.bcfinances.event.ResourceCreatedEvent;
import br.com.bcfinances.exceptionHandler.BcFinancesExceptionHandler.Error;
import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.repository.filter.TransactionFilter;
import br.com.bcfinances.repository.projection.TransactionSummary;
import br.com.bcfinances.service.TransactionService;
import br.com.bcfinances.service.exception.PersonInexistentOrInactiveException;
import br.com.bcfinances.storage.S3;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ApplicationEventPublisher publisher;
    private final MessageSource messageSource;
    private final S3 s3;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public Page<Transaction> list(@ModelAttribute TransactionFilter transactionFilter, Pageable pageable) {
        return transactionService.findAll(transactionFilter, pageable);
    }

    @GetMapping(params = "summary")// Parametro de decisão para escolher este endpoint
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public Page<TransactionSummary> sumUp(@ModelAttribute TransactionFilter transactionFilter, Pageable pageable) {
        return transactionService.sumUp(transactionFilter, pageable);
    }

    /********************************************************************
     * Retornar as estatísticas de lançamentos por categoria do mês atual
     * Poderia receber uma data como parâmetro e retornar com base nessa data
     * ******************************************************************/
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    @GetMapping("/statistics/category")
    public List<TransactionStatisticCategory> findByCategory() {
        return transactionService.findByCategory(LocalDate.now());
    }

    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    @GetMapping("/statistics/day")
    public List<TransactionStatisticByDay> findByDay() {
        return transactionService.findByDay(LocalDate.now());
    }

    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    @GetMapping("/reports/person")
    public ResponseEntity<byte[]> reportByPerson(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws JRException {
        byte[] report = transactionService.reportByPerson(start, end);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(report);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Transaction> launch = transactionService.findById(id);
        return launch.isPresent() ? ResponseEntity.ok(launch.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public ResponseEntity<Transaction> save(@Valid @RequestBody Transaction launch, HttpServletResponse response) {
        Transaction launchSave = transactionService.save(launch);
        publisher.publishEvent(new ResourceCreatedEvent(this, response, launchSave.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(launchSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Sucesso porém sem conteúdo
    @PreAuthorize("hasAuthority('ROLE_REMOVE_TRANSACTION')")
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public ResponseEntity<Transaction> update(@PathVariable Long id, @Valid @RequestBody Transaction launch) {
        try {
            Transaction launchSaved = transactionService.update(id, launch);
            return ResponseEntity.ok(launchSaved);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/attachment")
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public Attachment uploadAttachment(@RequestParam MultipartFile attachment) throws IOException {
//        OutputStream out = new FileOutputStream("/home/brunocesar/Documents/anexo--" + attachment.getOriginalFilename());
//        out.write(attachment.getBytes());
//        out.close();
        String name = s3.saveTemp(attachment);
        return new Attachment(name, s3.configureUrl(name));
    }

    /*Como é um tratamento particular de Lançamento pode ser tratado no próprio controlador*/
    @ExceptionHandler({PersonInexistentOrInactiveException.class})
    public ResponseEntity<Object> handlePersonInexistentOrInactiveException(PersonInexistentOrInactiveException ex) {
        String msgUser = messageSource.getMessage("person.not-exists-or-inactive", null, LocaleContextHolder.getLocale());
        String msgDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<Error> errors = Collections.singletonList(new Error(msgUser, msgDev));

        return ResponseEntity.badRequest().body(errors);
    }
}
