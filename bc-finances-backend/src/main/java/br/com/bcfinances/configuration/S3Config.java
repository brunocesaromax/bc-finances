package br.com.bcfinances.configuration;

import br.com.bcfinances.configuration.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketLifecycleConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ExpirationStatus;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.LifecycleExpiration;
import software.amazon.awssdk.services.s3.model.LifecycleRule;
import software.amazon.awssdk.services.s3.model.LifecycleRuleFilter;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketLifecycleConfigurationRequest;
import software.amazon.awssdk.services.s3.model.Tag;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final ApiProperty apiProperty;
    private final MessageSource messageSource;

    @Bean
    public S3Client s3Client() {
        // Verificar se as credenciais AWS estão configuradas com valores reais
        String accessKeyId = apiProperty.getS3().getAccessKeyId();
        String secretAccessKey = apiProperty.getS3().getSecretAccessKey();

        // Verificar se são placeholders do .env.example
        boolean isPlaceholder = isPlaceholderCredentials(accessKeyId, secretAccessKey);

        if (accessKeyId == null || accessKeyId.isEmpty() ||
            secretAccessKey == null || secretAccessKey.isEmpty() || isPlaceholder) {

            log.warn("AWS S3 não configurado (credenciais vazias ou placeholder).");

            // Retorna um cliente válido mas com credenciais placeholder para evitar falhas
            return S3Client.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("placeholder", "placeholder")))
                    .build();
        }

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_2)
                .build();
    }

    private boolean isPlaceholderCredentials(String accessKeyId, String secretAccessKey) {
        return "s3-access-key".equals(accessKeyId) ||
               "s3-secret-key".equals(secretAccessKey) ||
               "your-aws-access-key-id".equals(accessKeyId) ||
               "your-aws-secret-access-key".equals(secretAccessKey) ||
               "placeholder".equals(accessKeyId) ||
               "placeholder".equals(secretAccessKey);
    }

    /**
     * Método utilitário para configurar bucket sob demanda
     */
    public void ensureBucketExists(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            // Bucket não existe, criar
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());

            // Configurar regra de lifecycle
            LifecycleRule expireRule = LifecycleRule.builder()
                    .id("ExpireTemporaryFiles")
                    .filter(LifecycleRuleFilter.builder()
                            .tag(Tag.builder().key("expire").value("true").build())
                            .build())
                    .expiration(LifecycleExpiration.builder().days(1).build())
                    .status(ExpirationStatus.ENABLED)
                    .build();

            BucketLifecycleConfiguration lifecycleConfiguration = BucketLifecycleConfiguration.builder()
                    .rules(expireRule)
                    .build();

            s3Client.putBucketLifecycleConfiguration(PutBucketLifecycleConfigurationRequest.builder()
                    .bucket(bucketName)
                    .lifecycleConfiguration(lifecycleConfiguration)
                    .build());
        }
    }
}
