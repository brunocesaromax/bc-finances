package br.com.bcfinances.service;

import br.com.bcfinances.dto.TransactionStatisticByDay;
import br.com.bcfinances.dto.TransactionStatisticCategory;
import br.com.bcfinances.dto.TransactionStatisticPerson;
import br.com.bcfinances.mail.Mailer;
import br.com.bcfinances.model.Transaction;
import br.com.bcfinances.model.Transaction_;
import br.com.bcfinances.model.Person;
import br.com.bcfinances.model.User;
import br.com.bcfinances.repository.TransactionRepository;
import br.com.bcfinances.repository.UserRepository;
import br.com.bcfinances.repository.filter.TransactionFilter;
import br.com.bcfinances.repository.projection.TransactionSummary;
import br.com.bcfinances.service.exception.PersonInexistentOrInactiveException;
import br.com.bcfinances.storage.S3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private static final String RECIPIENTS = "ROLE_SEARCH_TRANSACTION";

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private final Mailer mailer;
    private final S3 s3;

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public Page<Transaction> findAll(TransactionFilter transactionFilter, Pageable pageable) {
        return transactionRepository.filterOut(transactionFilter, pageable);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        validatePerson(transaction.getPerson());

        if (StringUtils.hasText(transaction.getAttachment())) {
            s3.save(transaction.getAttachment());
        }

        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    public Page<TransactionSummary> sumUp(TransactionFilter transactionFilter, Pageable pageable) {
        return transactionRepository.sumUp(transactionFilter, pageable);
    }

    public Transaction update(Long id, Transaction transaction) {
        Transaction transactionBD = findExistentTransaction(id);

        if (!transaction.getPerson().equals(transactionBD.getPerson())) {
            validatePerson(transaction.getPerson());
        }

        if (StringUtils.isEmpty(transaction.getAttachment())
            && StringUtils.hasText(transactionBD.getAttachment())) {
            s3.delete(transactionBD.getAttachment());

        } else if (StringUtils.hasText(transaction.getAttachment())
                   && !transaction.getAttachment().equals(transactionBD.getAttachment())) {
            s3.update(transactionBD.getAttachment(), transaction.getAttachment());
        }

        BeanUtils.copyProperties(transaction, transactionBD, Transaction_.ID);
        return transactionRepository.save(transactionBD);
    }

    private void validatePerson(Person person) {
        if (Optional.ofNullable(person).map(Person::getId).isPresent()) {
            person = personService.findById(person.getId());
        }

        if (!Optional.ofNullable(person).isPresent() || person.isInactive()) {
            throw new PersonInexistentOrInactiveException();
        }
    }

    private Transaction findExistentTransaction(Long id) {
        Optional<Transaction> transactionBD = transactionRepository.findById(id);

        if (!transactionBD.isPresent()) {
            throw new IllegalArgumentException();
        } else {
            return transactionBD.get();
        }
    }

    public byte[] reportByPerson(LocalDate start, LocalDate end) throws JRException {
        List<TransactionStatisticPerson> result = transactionRepository.findByPerson(start, end);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DT_BEGIN", Date.valueOf(start));
        parameters.put("DT_END", Date.valueOf(end));
        parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/reports/launchs-by-person.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JRBeanCollectionDataSource(result));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    //    @Scheduled(fixedDelay = 1000 * 60 * 30) //Evita enfileiramento de execuções do método, somente quando uma execução termina que outra começa
    @Scheduled(cron = "0 0 6 * * *") // SS MM HH DAY_OF_MONTH MONTH DAY_OF_WEEK
    public void alertOverduetransactions() {
        if (log.isDebugEnabled()) {
            log.debug("Preparando envio de e-mails de aviso de lançamentos vencidos.");
        }

        List<Transaction> overduetransactions = transactionRepository.findByDueDayLessThanEqualAndPaydayIsNull(LocalDate.now());

        if (overduetransactions.isEmpty()) {
            log.info("Sem lançamentos vencidos para aviso.");
            return;
        }

        log.info("Existem {} lançamentos vencidos.", overduetransactions.size());

        List<User> users = userRepository.findByPermissionsDescription(RECIPIENTS);

        if (users.isEmpty()) {
            log.warn("Existem lançamentos vencidos, mas o sistema não encontrou destinatários.");
            return;
        }

        mailer.alertOverdueTransactions(overduetransactions, users);

        log.info("Envio de e-mails de aviso concluído.");
    }

    @Transactional(readOnly = true)
    public boolean existsWithPersonId(Long id) {
        return transactionRepository.existsByPersonId(id);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticCategory> findByCategory(LocalDate date) {
        return transactionRepository.findByCategory(date);
    }

    @Transactional(readOnly = true)
    public List<TransactionStatisticByDay> findByDay(LocalDate date) {
        return transactionRepository.findByDay(date);
    }
}
