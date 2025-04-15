package br.com.fiap.armazenamento_video.infra.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.amazonaws.services.s3.AmazonS3;

@SpringBootTest(classes = S3Config.class)
@TestPropertySource(properties = {
        "cloud.aws.credentials.access-key=test-access-key",
        "cloud.aws.credentials.secret-key=test-secret-key",
        "cloud.aws.region.static=us-east-1"
})
class S3ConfigTest {

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void deveInstanciarBeanAmazonS3() {
        assertNotNull(amazonS3);
    }
}
