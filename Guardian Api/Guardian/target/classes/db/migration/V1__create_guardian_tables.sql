/*
 * Copyright (c) 2026, Cbobio S.A. and/or its affiliates. All rights reserved.
 */

CREATE TABLE tb_role (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    roles_usuario VARCHAR(50) NOT NULL,
    descricao VARCHAR(250) NOT NULL,

    CONSTRAINT uk_role_usuario
        UNIQUE (roles_usuario)
);

CREATE TABLE tb_loja (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    numero_loja VARCHAR(4) NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    razao_social VARCHAR(200) NOT NULL,
    cnpj VARCHAR(14) NOT NULL,

    logradouro VARCHAR(200),
    numero VARCHAR(10),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf VARCHAR(2),
    cep VARCHAR(8),
    telefone VARCHAR(20),
    email VARCHAR(150),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    controle_produtos_alto_risco BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_loja_numero
        UNIQUE (numero_loja),

    CONSTRAINT uk_loja_cnpj
        UNIQUE (cnpj),

    CONSTRAINT ck_loja_numero
        CHECK (char_length(numero_loja) = 4),

    CONSTRAINT ck_loja_cnpj
        CHECK (char_length(cnpj) = 14),

    CONSTRAINT ck_loja_uf
        CHECK (uf IS NULL OR char_length(uf) = 2),

    CONSTRAINT ck_loja_cep
        CHECK (cep IS NULL OR char_length(cep) = 8)
);

CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(250),

    alto_risco BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_categoria_nome
        UNIQUE (nome)
);

CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    matricula VARCHAR(20) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(255) NOT NULL,

    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    bloqueado BOOLEAN NOT NULL DEFAULT FALSE,
    ultimo_login TIMESTAMP,

    loja_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT uk_usuario_matricula
        UNIQUE (matricula),

    CONSTRAINT uk_usuario_email
        UNIQUE (email),

    CONSTRAINT fk_usuario_loja
        FOREIGN KEY (loja_id)
        REFERENCES tb_loja (id),

    CONSTRAINT fk_usuario_role
        FOREIGN KEY (role_id)
        REFERENCES tb_role (id)
);

CREATE TABLE tb_produto (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    codigo_barras VARCHAR(14) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(250),
    marca VARCHAR(100) NOT NULL,
    custo NUMERIC(15,2) NOT NULL,

    alto_risco BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    categoria_id BIGINT NOT NULL,

    CONSTRAINT uk_produto_codigo_barras
        UNIQUE (codigo_barras),

    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES tb_categoria (id),

    CONSTRAINT ck_produto_custo
        CHECK (custo >= 0),

    CONSTRAINT ck_produto_codigo_barras
        CHECK (
            char_length(codigo_barras) BETWEEN 8 AND 14
        )
);

CREATE TABLE tb_estoque (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    quantidade INTEGER NOT NULL DEFAULT 0,
    quantidade_minima INTEGER NOT NULL DEFAULT 0,

    loja_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,

    CONSTRAINT uk_estoque_loja_produto
        UNIQUE (loja_id, produto_id),

    CONSTRAINT fk_estoque_loja
        FOREIGN KEY (loja_id)
        REFERENCES tb_loja (id),

    CONSTRAINT fk_estoque_produto
        FOREIGN KEY (produto_id)
        REFERENCES tb_produto (id),

    CONSTRAINT ck_estoque_quantidade
        CHECK (quantidade >= 0),

    CONSTRAINT ck_estoque_quantidade_minima
        CHECK (quantidade_minima >= 0)
);

CREATE TABLE tb_movimentacao_produto (
    id BIGSERIAL PRIMARY KEY,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,

    evento VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    motivo VARCHAR(30) NOT NULL,

    quantidade INTEGER NOT NULL,
    data_movimentacao TIMESTAMP NOT NULL,

    observacao VARCHAR(500),
    estado_anterior VARCHAR(20),

    produto_id BIGINT NOT NULL,
    loja_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,

    CONSTRAINT fk_movimentacao_produto
        FOREIGN KEY (produto_id)
        REFERENCES tb_produto (id),

    CONSTRAINT fk_movimentacao_loja
        FOREIGN KEY (loja_id)
        REFERENCES tb_loja (id),

    CONSTRAINT fk_movimentacao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES tb_usuario (id),

    CONSTRAINT ck_movimentacao_quantidade
        CHECK (quantidade > 0)
);

CREATE INDEX idx_mov_produto
    ON tb_movimentacao_produto (produto_id);

CREATE INDEX idx_mov_loja
    ON tb_movimentacao_produto (loja_id);

CREATE INDEX idx_mov_usuario
    ON tb_movimentacao_produto (usuario_id);

CREATE INDEX idx_mov_estado
    ON tb_movimentacao_produto (estado);

CREATE INDEX idx_mov_data
    ON tb_movimentacao_produto (data_movimentacao);

CREATE INDEX idx_mov_loja_estado
    ON tb_movimentacao_produto (loja_id, estado);

CREATE INDEX idx_mov_estado_anterior
    ON tb_movimentacao_produto (estado_anterior);