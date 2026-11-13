/*
 * Impede mais de um retorno ao estoque para a mesma devolução.
 */

CREATE UNIQUE INDEX uk_mov_retorno_estoque_origem
    ON tb_movimentacao_produto (movimentacao_origem_id)
    WHERE evento = 'RETORNAR_ESTOQUE'
      AND movimentacao_origem_id IS NOT NULL;