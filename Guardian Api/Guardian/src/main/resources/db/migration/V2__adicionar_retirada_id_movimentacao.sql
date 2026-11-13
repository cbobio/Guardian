ALTER TABLE tb_movimentacao_produto
    ADD COLUMN retirada_id BIGINT;

ALTER TABLE tb_movimentacao_produto
    ADD CONSTRAINT fk_movimentacao_retirada
    FOREIGN KEY (retirada_id)
    REFERENCES tb_movimentacao_produto (id);

CREATE INDEX idx_movimentacao_retirada
    ON tb_movimentacao_produto (retirada_id);