package br.com.bcfinances.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import br.com.bcfinances.configuration.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final ApiProperty apiProperty;
    private final MessageSource messageSource;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(
                apiProperty.getS3().getAccessKeyId(), apiProperty.getS3().getSecretAccessKey());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        //Criando e configurando o bucket de forma programada
        if (!amazonS3.doesBucketExistV2(apiProperty.getS3().getBucket())) {
            amazonS3.createBucket(new CreateBucketRequest(apiProperty.getS3().getBucket()));

            BucketLifecycleConfiguration.Rule expireRule = new BucketLifecycleConfiguration.Rule()
                    .withId(messageSource.getMessage("s3.expire.rule.id", null, LocaleContextHolder.getLocale()))
                    .withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expire", "true"))))
                    .withExpirationInDays(1)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);

            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(expireRule);

            amazonS3.setBucketLifecycleConfiguration(apiProperty.getS3().getBucket(), configuration);
        }

        return amazonS3;
    }
}
