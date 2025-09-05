-- Tabela de usuário
CREATE TABLE usuario(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Categorias
CREATE TABLE categoria(
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    cor VARCHAR(50),
    UNIQUE(usuario_id, nome)
);

-- Tabela de Orçamentos
CREATE TABLE orcamento(
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
    consumido NUMERIC(15,2) NOT NULL DEFAULT 0 CHECK (consumido >= 0),
    categoria_id BIGINT NOT NULL REFERENCES categoria(id) ON DELETE CASCADE
);

-- Tabela de Receitas
CREATE TABLE receita(
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
    recorrente BOOLEAN DEFAULT FALSE,
    data_entrada DATE NOT NULL
);

-- Tabela de Despesa
CREATE TABLE despesa(
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    categoria_id BIGINT NOT NULL REFERENCES categoria(id) ON DELETE CASCADE,
    valor NUMERIC(15,2) NOT NULL CHECK (valor >= 0),
    nome VARCHAR(255),
    descricao TEXT,
    recorrente BOOLEAN DEFAULT FALSE,
    data DATE NOT NULL,
    parcelas_totais INT,
    parcelas_restante INT
);