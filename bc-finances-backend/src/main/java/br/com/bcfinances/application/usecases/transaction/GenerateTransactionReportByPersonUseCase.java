package br.com.bcfinances.application.usecases.transaction;

import br.com.bcfinances.domain.repositories.TransactionRepository;
import br.com.bcfinances.application.dto.TransactionStatisticPersonDto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GenerateTransactionReportByPersonUseCase {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public byte[] execute(LocalDate start, LocalDate end) throws JRException {
        List<TransactionStatisticPersonDto> data = transactionRepository.findStatisticsByPerson(start, end);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("DT_BEGIN", Date.valueOf(start));
        parameters.put("DT_END", Date.valueOf(end));
        parameters.put("REPORT_LOCALE", Locale.of("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/reports/launchs-by-person.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JRBeanCollectionDataSource(data));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}