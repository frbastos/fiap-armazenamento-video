package br.com.fiap.armazenamento_video.domain.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ZipGeradoEvent {

    private String videoId;
    private String path;

}
