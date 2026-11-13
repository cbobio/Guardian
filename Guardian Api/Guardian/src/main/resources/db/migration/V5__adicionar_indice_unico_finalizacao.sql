/*
 * Garante que cada movimentação de entrega
 * tenha no máximo uma finalização registrada.
 */

CREATE UNIQUE INDEX uk_mov_finalizar_origem
    ON tb_movimentacao_produto (movimentacao_origem_id)
    WHERE evento = 'FINALIZAR'
      AND movimentacao_origem_id IS NOT NULL;