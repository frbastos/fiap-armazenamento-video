package br.com.fiap.armazenamento_video.infra.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.armazenamento_video.application.service.ArmazenadorVideoService;
import br.com.fiap.armazenamento_video.domain.event.StatusVideoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusVideoListener {

    private final ArmazenadorVideoService armazenadorVideoService;

    @RabbitListener(queues = "${queue.video}")
    public void receberStatusVideo(String evento) {
        StatusVideoEvent statusVideoEvent = toObject(evento);
        if ("CONCLUIDO".equalsIgnoreCase(statusVideoEvent.getStatus().toString())) {
            armazenadorVideoService.processarVideoFinalizado(statusVideoEvent);
        } else {
            log.info("Ignorando status {}", statusVideoEvent.getStatus());
        }
    }

    private StatusVideoEvent toObject(String evento) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(evento, StatusVideoEvent.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao receber mensagem status de video", e);
        }
    }

}
