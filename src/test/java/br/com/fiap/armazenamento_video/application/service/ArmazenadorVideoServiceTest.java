package br.com.fiap.armazenamento_video.application.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.fiap.armazenamento_video.application.gateway.PublicarZipGeradoGateway;
import br.com.fiap.armazenamento_video.domain.event.StatusVideoEvent;
import br.com.fiap.armazenamento_video.domain.event.ZipGeradoEvent;
import br.com.fiap.armazenamento_video.domain.usecase.GerarZipDosFramesUseCase;
import br.com.fiap.armazenamento_video.domain.valueobjects.VideoStatus;

class ArmazenadorVideoServiceTest {

    private GerarZipDosFramesUseCase gerarZipDosFramesUseCase;
    private PublicarZipGeradoGateway publicarZipGeradoGateway;
    private ArmazenadorVideoService armazenadorVideoService;

    @BeforeEach
    void setUp() {
        gerarZipDosFramesUseCase = mock(GerarZipDosFramesUseCase.class);
        publicarZipGeradoGateway = mock(PublicarZipGeradoGateway.class);
        armazenadorVideoService = new ArmazenadorVideoService(gerarZipDosFramesUseCase, publicarZipGeradoGateway);
    }

    @Test
    void deveProcessarVideoFinalizadoComSucesso() throws JsonProcessingException {
        // Arrange
        String videoId = "abc123";
        String zipPath = "abc123/zip/video.zip";
        StatusVideoEvent evento = new StatusVideoEvent(videoId, VideoStatus.CONCLUIDO);

        when(gerarZipDosFramesUseCase.gerar(videoId)).thenReturn(zipPath);

        // Act
        armazenadorVideoService.processarVideoFinalizado(evento);

        // Assert
        verify(gerarZipDosFramesUseCase).gerar(videoId);
        verify(publicarZipGeradoGateway).publicar(new ZipGeradoEvent(videoId, zipPath));
    }

    @Test
    void deveLancarRuntimeExceptionQuandoGerarZipLancarJsonProcessingException() throws JsonProcessingException {
        // Arrange
        String videoId = "erro1";
        StatusVideoEvent evento = new StatusVideoEvent(videoId, VideoStatus.CONCLUIDO);

        when(gerarZipDosFramesUseCase.gerar(videoId)).thenReturn("caminho/zip");
        doThrow(new JsonProcessingException("Erro JSON") {}).when(publicarZipGeradoGateway).publicar(any());

        assertThrows(RuntimeException.class, () -> {
            armazenadorVideoService.processarVideoFinalizado(evento);
        });

        verify(publicarZipGeradoGateway).publicar(any());
    }

    @Test
    void deveLancarRuntimeExceptionQuandoGerarZipLancarAmqpException() throws JsonProcessingException {
        // Arrange
        String videoId = "erro2";
        StatusVideoEvent evento = new StatusVideoEvent(videoId, VideoStatus.CONCLUIDO);

        when(gerarZipDosFramesUseCase.gerar(videoId)).thenReturn("caminho/zip");
        doThrow(new AmqpException("Erro AMQP")).when(publicarZipGeradoGateway).publicar(any());

        assertThrows(RuntimeException.class, () -> {
            armazenadorVideoService.processarVideoFinalizado(evento);
        });

        verify(publicarZipGeradoGateway).publicar(any());
    }
}