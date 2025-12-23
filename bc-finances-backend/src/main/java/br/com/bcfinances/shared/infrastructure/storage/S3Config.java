package br.com.bcfinances.shared.infrastructure.storage;

import br.com.bcfinances.shared.infrastructure.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ExpirationStatus;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.LifecycleRule;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketLifecycleConfigurationRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final ApiProperty apiProperty;

    @Bean
    public S3Client s3Client() {
        S3ClientBuilder builder = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
                .region(Region.of(apiProperty.getS3().getRegion()));

        builder = builder
                .endpointOverride(URI.create(apiProperty.getS3().getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(apiProperty.getS3().isPathStyleAccess())
                        .build());

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        S3Presigner.Builder builder = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
                .region(Region.of(apiProperty.getS3().getRegion()));

        builder = builder
                .endpointOverride(URI.create(apiProperty.getS3().getEndpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(apiProperty.getS3().isPathStyleAccess())
                        .build());

        return builder.build();
    }

    private AwsCredentials getAwsCredentials() {
        String accessKeyId = apiProperty.getS3().getAccessKeyId();
        String secretAccessKey = apiProperty.getS3().getSecretAccessKey();

        return AwsBasicCredentials.create(accessKeyId,secretAccessKey);
    }

    /**
     * Método utilitário para configurar bucket sob demanda
     */
    public void ensureBucketExists(S3Client s3Client, String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            // Bucket does not exist, create
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());

            // Configure Lifecycle Rule
            LifecycleRule expireRule = LifecycleRule.builder()
                    .id("ExpireTemporaryFiles")
                    .filter(lrf -> lrf.tag(
                                    tag -> tag.key("expire").value("true").build())
                            .build())
                    .expiration(le -> le.days(1).build())
                    .status(ExpirationStatus.ENABLED)
                    .build();

            s3Client.putBucketLifecycleConfiguration(PutBucketLifecycleConfigurationRequest.builder()
                    .bucket(bucketName)
                    .lifecycleConfiguration(lc -> lc.rules(expireRule).build())
                    .build());
        }
    }
}
