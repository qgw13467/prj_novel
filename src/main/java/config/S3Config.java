package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	 @Value("${cloud.aws.credentials.access-key}")
	    private String accessKey;

	    @Value("${cloud.aws.credentials.secret-key}")
	    private String secretKey;

	    @Value("${cloud.aws.region.static}")
	    private String region;

	    @Bean
	    public S3Config amazonS3Client() {
	        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
	        return (S3Config) AmazonS3ClientBuilder.standard()
	                .withRegion(region)
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .build();
	    }
	
}
