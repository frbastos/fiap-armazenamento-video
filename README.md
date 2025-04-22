
# FIAP - Arquitetura de Software -> Sistema de Processamento de Video

## Desafio

O projeto desenvolvido está sem nenhuma das boas práticas de arquitetura de software que nós
aprendemos no curso.
O seu desafio será desenvolver uma aplicação utilizando os conceitos apresentados no curso como:
desenho de arquitetura, desenvolvimento de microsservicos, Qualidade de Software, Mensageria …etc
E para ajudar o seu grupo nesta etapa de levantamento de requisitos, segue alguns dos pré
requisitos esperados para este projeto:

- A nova versão do sistema deve processar mais de um vídeo ao mesmo tempo;
- Em caso de picos o sistema não deve perder uma requisição;
- O Sistema deve ser protegido por usuário e senha;
- O fluxo deve ter uma listagem de status dos vídeos de um usuário;
- Em caso de erro um usuário pode ser notificado (email ou um outro meio de comunicação)

Requisitos técnicos:

- O sistema deve persistir os dados;
- O sistema deve estar em uma arquitetura que o permita ser escalado;
- O projeto deve ser versionado no Github;
- O projeto deve ter testes que garantam a sua qualidade;
- CI/CD da aplicacao

## Funcionalidades

Esse microserviço é responsável por receber as mensagens na fila e zipar os frames extraído que estão armazenados no S3 da AWS.

## Diagrama da Arquitetura

![Diagrama da Arquitetura](./docs/fluxo_microsservicos.png)