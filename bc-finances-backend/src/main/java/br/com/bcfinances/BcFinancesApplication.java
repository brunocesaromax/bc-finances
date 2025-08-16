package br.com.bcfinances;

import br.com.bcfinances.configuration.property.ApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApiProperty.class)
public class BcFinancesApplication {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void main(String[] args) {
        APPLICATION_CONTEXT = SpringApplication.run(BcFinancesApplication.class, args);
    }

    public static <T> T getBean(Class<T> type) {
        return APPLICATION_CONTEXT.getBean(type);
    }
}
