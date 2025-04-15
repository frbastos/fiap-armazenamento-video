package br.com.fiap.armazenamento_video.infra.gateway;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.com.fiap.armazenamento_video.application.gateway.ArmazenamentoGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArmazenamentoS3Gateway implements ArmazenamentoGateway {

    private final AmazonS3 s3;

    @Value("${storage.s3.bucket}")
    private String bucket;

    @Override
    public List<String> listarArquivos(String prefixo) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(prefixo);

        ListObjectsV2Result result = s3.listObjectsV2(request);

        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public String gerarZip(String prefixo, List<String> arquivos, String destinoZip) {
        try {
            File zipFile = File.createTempFile("video", ".zip");

            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
                for (String key : arquivos) {
                    S3Object s3Object = s3.getObject(bucket, key);

                    try (InputStream inputStream = s3Object.getObjectContent()) {
                        String nomeNoZip = key.replace(prefixo, "");
                        zipOut.putNextEntry(new ZipEntry(nomeNoZip));
                        inputStream.transferTo(zipOut);
                        zipOut.closeEntry();
                    }
                }
            }

            // Upload do zip gerado
            s3.putObject(bucket, destinoZip, zipFile);
            zipFile.delete();

            return destinoZip;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar zip no S3", e);
        }
    }

}
