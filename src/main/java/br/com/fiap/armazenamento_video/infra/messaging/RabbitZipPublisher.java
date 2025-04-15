package br.com.fiap.armazenamento_video.infra.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.armazenamento_video.application.gateway.PublicarZipGeradoGateway;
import br.com.fiap.armazenamento_video.domain.event.ZipGeradoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitZipPublisher implements PublicarZipGeradoGateway {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.zip}")
    private String queue;

    @Override
    public void publicar(ZipGeradoEvent event) throws JsonProcessingException, AmqpException {
        rabbitTemplate.convertAndSend(queue, toMessage(event));
    }

    private String toMessage(ZipGeradoEvent payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(payload);
    }

}
