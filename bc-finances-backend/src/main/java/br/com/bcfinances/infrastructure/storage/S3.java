package br.com.bcfinances.infrastructure.storage;

import br.com.bcfinances.infrastructure.config.S3Config;
import br.com.bcfinances.infrastructure.config.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3 {

    private final S3Client s3Client;
    private final ApiProperty apiProperty;
    private final S3Config s3Config;
    private final MessageSource messageSource;

    public String saveTemp(MultipartFile file) {
        // Verificar se S3 está configurado com credenciais reais
        if (!isS3Available()) {
            log.warn("S3 não configurado. Upload do arquivo '{}' será ignorado.", file.getOriginalFilename());
            return generateUniqueName(file.getOriginalFilename()); // Retorna nome único mas não faz upload
        }
        
        // Garantir que o bucket existe antes de usar
        ensureBucketConfigured();
        
        String uniqueName = generateUniqueName(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(apiProperty.getS3().getBucket())
                    .key(uniqueName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .tagging(Tagging.builder()
                            .tagSet(Tag.builder().key("expire").value("true").build())
                            .build())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage("s3.success.upload", null, LocaleContextHolder.getLocale()),
                        file.getOriginalFilename());
            }

            return uniqueName;
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage("s3.fail.upload", null, LocaleContextHolder.getLocale()), e);
        }
    }


    public String configureUrl(String object) {
        // o '\\\\' não importará se o protocolo é http ou https
        // o '\\\\' só será utilizado no contexto do cliente
        return "\\\\" + apiProperty.getS3().getBucket() + ".s3.amazonaws.com/" + object;
    }

    //Salvar arquivo temporário como permanente
    public void save(String object) {
        if (!isS3Available()) {
            log.warn("S3 não configurado. Operação save ignorada para objeto: {}", object);
            return;
        }
        
        PutObjectTaggingRequest putObjectTaggingRequest = PutObjectTaggingRequest.builder()
                .bucket(apiProperty.getS3().getBucket())
                .key(object)
                .tagging(Tagging.builder().tagSet(Collections.emptyList()).build())
                .build();

        s3Client.putObjectTagging(putObjectTaggingRequest);
    }

    public void update(String oldObject, String newObject) {
        if (StringUtils.hasText(oldObject)) {
            delete(oldObject);
        }

        save(newObject);
    }

    public void delete(String object) {
        if (!isS3Available()) {
            log.warn("S3 não configurado. Operação delete ignorada para objeto: {}", object);
            return;
        }
        
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(apiProperty.getS3().getBucket())
                .key(object)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String generateUniqueName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
    
    private boolean isS3Available() {
        String accessKeyId = apiProperty.getS3().getAccessKeyId();
        String secretAccessKey = apiProperty.getS3().getSecretAccessKey();
        
        // Verificar se são credenciais placeholder
        return !(accessKeyId == null || accessKeyId.isEmpty() || 
                secretAccessKey == null || secretAccessKey.isEmpty() ||
                "placeholder".equals(accessKeyId) || "placeholder".equals(secretAccessKey) ||
                "s3-access-key".equals(accessKeyId) || "s3-secret-key".equals(secretAccessKey) ||
                "your-aws-access-key-id".equals(accessKeyId) || "your-aws-secret-access-key".equals(secretAccessKey));
    }
    
    private void ensureBucketConfigured() {
        if (!isS3Available()) {
            return; // S3 não configurado, nada a fazer
        }
        
        try {
            s3Config.ensureBucketExists(s3Client, apiProperty.getS3().getBucket());
        } catch (Exception e) {
            log.warn("Could not configure S3 bucket: {}. S3 functionality may not work properly.", e.getMessage());
            // Não lança exceção para permitir que a aplicação continue funcionando sem S3
        }
    }
}
