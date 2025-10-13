# Regras de Negócio - WeatherApp

## Fluxo e Lógica
- O app inicia na Tela Principal exibindo cidade atual e clima atual.
- Usuário pode abrir Detalhes para visualizar lista de previsão.
- Em Configurações o usuário pode alterar cidade e período da previsão.
- Seleção de Cidade salva a preferência localmente (mock nesta fase).
- Seleção de Período define se a previsão será semanal/quinzenal/mensal (mock nesta fase).

## Regras
- O app não requer login nesta fase (protótipo).
- A cidade deve ter ao menos 2 caracteres para ser salva.
- O período deve ser um dos valores: semanal, quinzenal, mensal.
- Sem permissões de localização nesta fase; apenas seleção manual.

## Validações
- Exibir aviso se o usuário tentar salvar cidade vazia.
- Exibir aviso se nenhum período for selecionado.

## Exemplo
- "O usuário só pode confirmar o período se um item estiver selecionado."