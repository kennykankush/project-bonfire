package ai.lumidah.bonfire_v1_app_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

    @Value("${s3.access}")
    private String access;

    @Value("${s3.secret}")
    private String secret;

    @Value("${s3.endpoint}")
    private String endpoint;

    @Bean   
    public AmazonS3 getS3Client(){
        BasicAWSCredentials cred = new BasicAWSCredentials(access, secret);

        EndpointConfiguration epConfiguration = new EndpointConfiguration(endpoint, "sgp1");

        return AmazonS3ClientBuilder.standard().withEndpointConfiguration(epConfiguration).withCredentials(new AWSStaticCredentialsProvider(cred)).build();
    }
}