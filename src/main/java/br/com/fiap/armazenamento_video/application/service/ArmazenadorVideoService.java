package br.com.fiap.armazenamento_video.application.service;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.fiap.armazenamento_video.application.gateway.PublicarZipGeradoGateway;
import br.com.fiap.armazenamento_video.domain.event.StatusVideoEvent;
import br.com.fiap.armazenamento_video.domain.event.ZipGeradoEvent;
import br.com.fiap.armazenamento_video.domain.usecase.GerarZipDosFramesUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArmazenadorVideoService {

    private final GerarZipDosFramesUseCase gerarZip;
    private final PublicarZipGeradoGateway publicarZip;

    public void processarVideoFinalizado(StatusVideoEvent evento) {
        String videoId = evento.getVideoId();
        try {
            String zipPath = gerarZip.gerar(videoId);
            publicarZip.publicar(new ZipGeradoEvent(videoId, zipPath));
        } catch (JsonProcessingException | AmqpException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao publicar evento do vídeo", e);
        }
    }
}