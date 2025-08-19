package org.layer.storage.service;

import static org.layer.global.exception.StorageExceptionType.*;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.domain.common.random.CustomRandom;
import org.layer.domain.space.entity.SpaceField;
import org.layer.storage.dto.StorageResponse;
import org.layer.storage.enums.ImageDomain;
import org.layer.storage.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {

    @Value("${ncp.storage.bucketName}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;
    private final CustomRandom random;

    public StorageResponse.PresignedResult getPreSignedUrl(Long memberId, ImageDomain imageDomain) {
        String imagePath = imageDomain + "/" + memberId.toString() + "/" + UUID.randomUUID();
        var imageUrl = amazonS3Client.getUrl(bucketName, imagePath);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(imagePath);

        return StorageResponse.PresignedResult.toResponse(
                amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString(),
                imageUrl.toString()
        );
    }

    public boolean validateBannerUrl(String url) {
        if(url == null){
            return false;
        }

        String objectKey = extractObjectKey(url);
        boolean isExist = amazonS3Client.doesObjectExist(bucketName, objectKey);
        if (!isExist) {
            throw new StorageException(OBJECT_INVALID_ERROR);
        }
        return true;
    }

    public String getDefaultBannerUrl(){
        String expectedPrefix = "https://" + bucketName + ".kr.object.ncloudstorage.com";

        int index = random.nextInt(SpaceField.values().length);
        return expectedPrefix + "/category/" + SpaceField.values()[index].getValue() + ".png";
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        // 15 초
        expTimeMillis += 1000 * 15;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String extractObjectKey(String url) {
        String expectedPrefix = "https://" + bucketName + ".kr.object.ncloudstorage.com";

        if (!url.startsWith(expectedPrefix)) {
            throw new StorageException(OBJECT_INVALID_ERROR);
        }
        return url.substring(expectedPrefix.length() + 1); // "/" 이후부터 추출
    }
}
