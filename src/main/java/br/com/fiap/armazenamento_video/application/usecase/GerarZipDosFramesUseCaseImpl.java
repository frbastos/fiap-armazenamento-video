package br.com.fiap.armazenamento_video.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.armazenamento_video.application.gateway.ArmazenamentoGateway;
import br.com.fiap.armazenamento_video.domain.usecase.GerarZipDosFramesUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GerarZipDosFramesUseCaseImpl implements GerarZipDosFramesUseCase {

    private final ArmazenamentoGateway armazenamentoGateway;

    @Override
    public String gerar(String videoId) {
        String prefixo = videoId + "/frames/";
        String destinoZip = videoId + "/zip/video.zip";

        // Lista arquivos do diretório de frames
        List<String> arquivos = armazenamentoGateway.listarArquivos(prefixo);

        if (arquivos.isEmpty()) {
            throw new IllegalStateException("Nenhum frame encontrado para o vídeo " + videoId);
        }

        // Gera o zip com base nos arquivos e salva no destino
        return armazenamentoGateway.gerarZip(prefixo, arquivos, destinoZip);
    }

}
