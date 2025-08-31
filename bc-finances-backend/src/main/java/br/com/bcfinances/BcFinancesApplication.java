package br.com.bcfinances;

import br.com.bcfinances.shared.infrastructure.config.property.ApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApiProperty.class)
public class BcFinancesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BcFinancesApplication.class, args);
    }
}
