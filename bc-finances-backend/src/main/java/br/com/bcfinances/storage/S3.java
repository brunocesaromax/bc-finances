package br.com.bcfinances.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import br.com.bcfinances.configuration.property.ApiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3 {

    private final AmazonS3 amazonS3;
    private final ApiProperty apiProperty;

    private final MessageSource messageSource;

    public String saveTemp(MultipartFile file) {
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String uniqueName = generateUniqueName(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    apiProperty.getS3().getBucket(),
                    uniqueName,
                    file.getInputStream(),
                    objectMetadata)
                    .withAccessControlList(acl);

            putObjectRequest.setTagging(
                    new ObjectTagging(Collections.singletonList(new Tag("expire", "true"))));

            amazonS3.putObject(putObjectRequest);

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
        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
                apiProperty.getS3().getBucket(),
                object,
                new ObjectTagging(Collections.emptyList())
        );

        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public void update(String oldObject, String newObject) {
        if (StringUtils.hasText(oldObject)) {
            delete(oldObject);
        }

        save(newObject);
    }

    public void delete(String object) {
        DeleteObjectRequest deleteObjectsRequest = new DeleteObjectRequest(apiProperty.getS3().getBucket(), object);
        amazonS3.deleteObject(deleteObjectsRequest);
    }

    private String generateUniqueName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}
