package br.com.fiap.armazenamento_video.application.gateway;

import org.springframework.amqp.AmqpException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.fiap.armazenamento_video.domain.event.ZipGeradoEvent;

public interface PublicarZipGeradoGateway {

    void publicar(ZipGeradoEvent event) throws JsonProcessingException, AmqpException;

}
