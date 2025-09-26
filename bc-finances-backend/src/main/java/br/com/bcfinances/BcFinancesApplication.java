package br.com.bcfinances;

import br.com.bcfinances.shared.infrastructure.property.ApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApiProperty.class)
@EnableCaching
public class BcFinancesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BcFinancesApplication.class, args);
    }
}
