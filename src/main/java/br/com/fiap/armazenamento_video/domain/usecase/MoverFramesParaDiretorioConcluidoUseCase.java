package br.com.fiap.armazenamento_video.domain.usecase;

import java.util.List;

public interface MoverFramesParaDiretorioConcluidoUseCase {

    List<String> mover(String videoId);

}
