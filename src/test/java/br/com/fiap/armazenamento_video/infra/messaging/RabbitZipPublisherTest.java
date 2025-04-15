package br.com.fiap.armazenamento_video.infra.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.fiap.armazenamento_video.domain.event.ZipGeradoEvent;

class RabbitZipPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitZipPublisher publisher;

    @BeforeEach
    void setUp() throws Exception {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new RabbitZipPublisher(rabbitTemplate);

        // Injeta o valor da queue com reflection
        var field = RabbitZipPublisher.class.getDeclaredField("queue");
        field.setAccessible(true);
        field.set(publisher, "queue.zip.test");
    }

    @Test
    void devePublicarMensagemNoRabbit() throws JsonProcessingException {
        // Arrange
        ZipGeradoEvent event = new ZipGeradoEvent("video123", "caminho/zip/video.zip");

        // Act
        publisher.publicar(event);

        // Assert
        verify(rabbitTemplate).convertAndSend(eq("queue.zip.test"), anyString());
    }

    @Test
    void deveLancarJsonProcessingExceptionSeObjetoNaoForSerializavel() {
        // Arrange
        ZipGeradoEvent event = mock(ZipGeradoEvent.class);
        try {
            when(event.getVideoId()).thenThrow(new RuntimeException("Erro inesperado ao serializar"));

            // Act & Assert
            assertThrows(JsonProcessingException.class, () -> publisher.publicar(event));
        } catch (AmqpException e) {
            fail("Erro AMQP não era esperado nesse teste");
        }
    }
}
