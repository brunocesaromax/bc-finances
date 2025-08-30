package br.com.bcfinances.mail;

import br.com.bcfinances.domain.entities.Transaction;
import br.com.bcfinances.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Mailer {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
//    private final TransactionRepository repository;

    //Usar para simular emails na aplicação
    /*@EventListener
    private void test(ApplicationReadyEvent event) {
        this.sendEmail("brunocesarjavadevtest@gmail.com",
                Collections.singletonList("brunocesar.oc96@gmail.com"),
                "Testando", "Olá!<br/>Teste ok.");
        System.out.println("TERMINADO O ENVIO DE EMAIL...");
    }*/

//    @EventListener
//    private void test(ApplicationReadyEvent event) {//Evento disparado quando aplicação sobe
//        String template = "mail/alert-overdue-transactions";
//
//        List<Transaction> transactionList = repository.findAll();
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("transactions", transactionList);
//
//        this.sendEmail("brunocesar.dev.test.java@gmail.com",
//                Collections.singletonList("brunocesar.oc96@gmail.com"),
//                "Testando", template, variables);
//        System.out.println("TERMINADO O ENVIO DE EMAIL...");
//    }

    public void sendEmail(String from, List<String> targets, String subject, String template, Map<String, Object> variables) {
        Context context = new Context(new Locale("pt", "BR"));

        variables.forEach(context::setVariable);

        String message = templateEngine.process(template, context);
        sendEmail(from, targets, subject, message);
    }

    public void alertOverdueTransactions(List<Transaction> transactions, List<User> recipients) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("transactions", transactions);

        List<String> emails = recipients.stream().map(User::getEmail).toList();
        this.sendEmail("brunocesar.dev.test.java@gmail.com",
                emails,
                "Lançamentos vencidos",
                "mail/alert-overdue-transactions",
                variables);
    }

    public void sendEmail(String from, List<String> targets, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setFrom(from);
            helper.setTo(targets.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}
