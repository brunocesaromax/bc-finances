package br.com.bcfinances.shared.infrastructure.mail;

import br.com.bcfinances.shared.infrastructure.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final ApiProperty apiProperty;

    @Bean
    public JavaMailSender javaMailSender() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.connectiontimeout", 10 * 1000); //10 segundos

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(props);
        mailSender.setHost(apiProperty.getMail().getHost());
        mailSender.setPort(Integer.parseInt(apiProperty.getMail().getPort()));
        mailSender.setUsername(apiProperty.getMail().getUsername());
        mailSender.setPassword(apiProperty.getMail().getPassword());

        return mailSender;
    }
}
