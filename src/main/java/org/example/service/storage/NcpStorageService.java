package org.example.service.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Member;
import org.example.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NcpStorageService {

    private final Environment env;
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";
    private final MemberRepository memberRepository;

    @Value("${spring.s3.accessKey}")
    private String accessKey;

    @Value("${spring.s3.secretKey}")
    private String secretKey;

    @Value("${spring.s3.bucket}")
    private String bucketName;


    private AmazonS3Client amazonS3Client;

    @PostConstruct
    private void init() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonS3Client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    } //일단 s3 client 생성

    public String imageUpload(MultipartFile profileImg) {
        String originalFileName = profileImg.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName); //파일명 유일하게
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(profileImg.getSize());
        objectMetadata.setContentType(profileImg.getContentType()); //타입 그대로 설정

        try (InputStream inputStream = profileImg.getInputStream()) {
            String keyName = "profile-image" + uploadFileName;

            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();
            log.info("image save clear");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFileUrl;
    }

    private String getUuidFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    } //파일명 unique화

    public void imageDelete(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow(() -> new IllegalArgumentException("Member not found for email: " + email));

        String profileImageUrl = member.get().getProfileImage();
        if (profileImageUrl == null || profileImageUrl.isEmpty()) {
            log.info("No profile image found,email: {}", email);
            return;
        }

        try {
            URI uri = new URI(profileImageUrl);
            String keyName = Paths.get(uri.getPath()).getFileName().toString();

            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            log.info("Image delete success!!, email: {}", email);
        } catch (Exception e) {
            log.error("Fail", e);
        }
    }


}
