package br.com.fiap.armazenamento_video.domain.event;

import br.com.fiap.armazenamento_video.domain.valueobjects.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StatusVideoEvent {

    private String videoId;
    private VideoStatus status;
}
