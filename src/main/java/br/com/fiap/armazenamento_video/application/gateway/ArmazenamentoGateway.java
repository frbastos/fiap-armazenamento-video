package br.com.fiap.armazenamento_video.application.gateway;

import java.util.List;

public interface ArmazenamentoGateway {

    String gerarZip(String prefixo, List<String> arquivos, String destinoZip);

    List<String> listarArquivos(String prefixo);

}
