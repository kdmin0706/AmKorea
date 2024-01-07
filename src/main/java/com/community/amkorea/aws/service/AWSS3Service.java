package com.community.amkorea.aws.service;

import static com.community.amkorea.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.community.amkorea.aws.dto.S3ImageDto;
import com.community.amkorea.global.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSS3Service {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public S3ImageDto uploadFile(MultipartFile multipartFile) {
    log.info("[uploadFile] start");

    String fileName = generateFileName(multipartFile);

    try (InputStream inputStream = multipartFile.getInputStream()) {
      amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, getObjectMetadata(multipartFile))
          .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch (IOException e) {
      log.error("IOException is occurred", e);
      throw new CustomException(INTERNAL_SERVER_ERROR);
    }

    return new S3ImageDto(getUrl(fileName), fileName, multipartFile.getSize());
  }

  private String getUrl(String fileName) {
    return amazonS3.getUrl(bucketName, fileName).toString();
  }

  public void deleteFile(String fileName) {
    try {
      amazonS3.deleteObject(bucketName, fileName);
    } catch (AmazonS3Exception e) {
      log.error("AmazonS3Exception is occurred", e);
      throw new CustomException(INTERNAL_SERVER_ERROR);
    }
  }

  private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());
    objectMetadata.setContentLength(multipartFile.getSize());
    return objectMetadata;
  }

  private String generateFileName(MultipartFile multipartFile) {
    return UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
  }

}
