package br.com.fiap.armazenamento_video.infra.listener;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.armazenamento_video.application.service.ArmazenadorVideoService;
import br.com.fiap.armazenamento_video.domain.event.StatusVideoEvent;
import br.com.fiap.armazenamento_video.domain.valueobjects.VideoStatus;

class StatusVideoListenerTest {

    private ArmazenadorVideoService armazenadorVideoService;
    private StatusVideoListener listener;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        armazenadorVideoService = mock(ArmazenadorVideoService.class);
        listener = new StatusVideoListener(armazenadorVideoService);
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveProcessarEventoQuandoStatusForConcluido() throws JsonProcessingException {
        // Arrange
        StatusVideoEvent evento = new StatusVideoEvent("video123", VideoStatus.CONCLUIDO);
        String payload = objectMapper.writeValueAsString(evento);

        // Act
        listener.receberStatusVideo(payload);

        // Assert
        verify(armazenadorVideoService).processarVideoFinalizado(evento);
    }

    @Test
    void deveIgnorarEventoQuandoStatusForErro() throws JsonProcessingException {
        // Arrange
        StatusVideoEvent evento = new StatusVideoEvent("video123", VideoStatus.ERRO);
        String payload = objectMapper.writeValueAsString(evento);

        // Act
        listener.receberStatusVideo(payload);

        // Assert
        verify(armazenadorVideoService, never()).processarVideoFinalizado(any());
    }

    @Test
    void deveLancarExcecaoQuandoJsonForInvalido() {
        // Arrange
        String jsonInvalido = "{ \"videoId\": \"video123\" "; // falta 'status' e fecha chave

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listener.receberStatusVideo(jsonInvalido);
        });

        assertTrue(exception.getMessage().contains("Erro ao receber mensagem status de video"));
        verifyNoInteractions(armazenadorVideoService);
    }
}
