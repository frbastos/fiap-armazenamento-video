package br.com.fiap.armazenamento_video.infra.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

class ArmazenamentoS3GatewayTest {

    private AmazonS3 s3;
    private ArmazenamentoS3Gateway gateway;

    @BeforeEach
    void setUp() {
        s3 = mock(AmazonS3.class);
        gateway = new ArmazenamentoS3Gateway(s3);

        // Como @Value não funciona em teste puro, vamos setar o campo com reflection:
        try {
            var bucketField = ArmazenamentoS3Gateway.class.getDeclaredField("bucket");
            bucketField.setAccessible(true);
            bucketField.set(gateway, "bucket-teste");
        } catch (Exception e) {
            fail("Erro ao injetar bucket");
        }
    }

    @Test
    void deveListarArquivosComPrefixo() {
        // Arrange
        ListObjectsV2Result resultMock = mock(ListObjectsV2Result.class);
        S3ObjectSummary summary1 = new S3ObjectSummary();
        summary1.setKey("prefixo/frame1.jpg");
        S3ObjectSummary summary2 = new S3ObjectSummary();
        summary2.setKey("prefixo/frame2.jpg");

        when(s3.listObjectsV2(any(ListObjectsV2Request.class)))
                .thenReturn(resultMock);
        when(resultMock.getObjectSummaries())
                .thenReturn(Arrays.asList(summary1, summary2));

        // Act
        List<String> arquivos = gateway.listarArquivos("prefixo/");

        // Assert
        assertEquals(2, arquivos.size());
        assertTrue(arquivos.contains("prefixo/frame1.jpg"));
        assertTrue(arquivos.contains("prefixo/frame2.jpg"));
    }

    @Test
    void deveGerarZipComSucesso() {
        // Arrange
        String prefixo = "video123/frames/";
        List<String> arquivos = Arrays.asList(
                "video123/frames/frame1.jpg",
                "video123/frames/frame2.jpg");
        String destinoZip = "video123/zip/video.zip";

        for (String key : arquivos) {
            S3Object s3Object = mock(S3Object.class);
            S3ObjectInputStream inputStream = new S3ObjectInputStream(
                    new ByteArrayInputStream("conteudo".getBytes()), null);
            when(s3Object.getObjectContent()).thenReturn(inputStream);
            when(s3.getObject("bucket-teste", key)).thenReturn(s3Object);
        }

        when(s3.putObject(eq("bucket-teste"), eq(destinoZip), any(File.class)))
                .thenReturn(new PutObjectResult());

        // Act
        String result = gateway.gerarZip(prefixo, arquivos, destinoZip);

        // Assert
        assertEquals(destinoZip, result);
        verify(s3, times(2)).getObject(eq("bucket-teste"), anyString());
        verify(s3).putObject(eq("bucket-teste"), eq(destinoZip), any(File.class));
    }
}
