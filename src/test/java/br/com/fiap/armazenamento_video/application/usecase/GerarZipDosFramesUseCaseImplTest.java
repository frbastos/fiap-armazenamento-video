package br.com.fiap.armazenamento_video.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.armazenamento_video.application.gateway.ArmazenamentoGateway;
import br.com.fiap.armazenamento_video.domain.usecase.GerarZipDosFramesUseCase;

class GerarZipDosFramesUseCaseImplTest {

    private ArmazenamentoGateway armazenamentoGateway;
    private GerarZipDosFramesUseCase gerarZipDosFramesUseCase;

    @BeforeEach
    void setUp() {
        armazenamentoGateway = mock(ArmazenamentoGateway.class);
        gerarZipDosFramesUseCase = new GerarZipDosFramesUseCaseImpl(armazenamentoGateway);
    }

    @Test
    void deveGerarZipComSucesso() {
        // Arrange
        String videoId = "123";
        String prefixo = "123/frames/";
        String destinoZip = "123/zip/video.zip";
        List<String> arquivos = Arrays.asList("frame1.jpg", "frame2.jpg");

        when(armazenamentoGateway.listarArquivos(prefixo)).thenReturn(arquivos);
        when(armazenamentoGateway.gerarZip(prefixo, arquivos, destinoZip)).thenReturn(destinoZip);

        // Act
        String resultado = gerarZipDosFramesUseCase.gerar(videoId);

        // Assert
        assertEquals(destinoZip, resultado);
        verify(armazenamentoGateway).listarArquivos(prefixo);
        verify(armazenamentoGateway).gerarZip(prefixo, arquivos, destinoZip);
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverFrames() {
        // Arrange
        String videoId = "123";
        String prefixo = "123/frames/";

        when(armazenamentoGateway.listarArquivos(prefixo)).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerarZipDosFramesUseCase.gerar(videoId);
        });

        assertEquals("Nenhum frame encontrado para o vídeo 123", exception.getMessage());
        verify(armazenamentoGateway).listarArquivos(prefixo);
        verify(armazenamentoGateway, never()).gerarZip(any(), any(), any());
    }

}
