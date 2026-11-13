ALTER TABLE tb_movimentacao_produto
ADD COLUMN movimentacao_origem_id BIGINT;

ALTER TABLE tb_movimentacao_produto
ADD CONSTRAINT fk_movimentacao_origem
FOREIGN KEY (movimentacao_origem_id)
REFERENCES tb_movimentacao_produto (id);

CREATE INDEX idx_movimentacao_origem_id
ON tb_movimentacao_produto (movimentacao_origem_id);