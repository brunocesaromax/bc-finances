package br.com.bcfinances.transaction.presentation.controllers;

import br.com.bcfinances.shared.infrastructure.event.ResourceCreatedEvent;
import br.com.bcfinances.shared.infrastructure.storage.S3Service;
import br.com.bcfinances.transaction.application.dto.AttachmentDto;
import br.com.bcfinances.transaction.application.dto.TransactionFilterDto;
import br.com.bcfinances.transaction.application.dto.TransactionRequest;
import br.com.bcfinances.transaction.application.dto.TransactionResponse;
import br.com.bcfinances.transaction.application.dto.TransactionSummaryDto;
import br.com.bcfinances.transaction.application.usecases.CreateTransactionUseCase;
import br.com.bcfinances.transaction.application.usecases.DeleteTransactionUseCase;
import br.com.bcfinances.transaction.application.usecases.FindTransactionByIdUseCase;
import br.com.bcfinances.transaction.application.usecases.UpdateTransactionUseCase;
import br.com.bcfinances.transaction.domain.contracts.TransactionRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final FindTransactionByIdUseCase findTransactionByIdUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final TransactionRepository transactionRepository;
    private final S3Service s3Service;
    private final ApplicationEventPublisher publisher;

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

    @GetMapping(params = "summary")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_TRANSACTION')")
    public Page<TransactionSummaryDto> summary(@ModelAttribute TransactionFilterDto filter, Pageable pageable) {
        return transactionRepository.findWithFilter(filter, pageable);
    }

    @PostMapping("/attachments")
    @PreAuthorize("hasAuthority('ROLE_CREATE_TRANSACTION')")
    public List<AttachmentDto> uploadAttachment(@RequestParam("attachments") List<MultipartFile> attachments) {
        List<MultipartFile> files = attachments != null ? attachments : List.of();

        return files.stream()
                .map(file -> {
                    String name = s3Service.saveTemp(file);
                    return new AttachmentDto(
                            name,
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getSize(),
                            s3Service.configureUrl(name)
                    );
                })
                .toList();
    }
}
