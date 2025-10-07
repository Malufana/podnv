# Para Onde Foi o Meu Dinheiro? (PODNV)

## 📄 Descrição

O **PODNV** é uma aplicação de controle financeiro pessoal que permite aos usuários gerenciar suas despesas, receitas, orçamentos e categorias de forma intuitiva, acompanhando de perto para onde está indo seu dinheiro. O projeto inclui funcionalidades para registro, edição, exclusão e análise de informações financeiras, ajudando o usuário a tomar decisões mais conscientes sobre suas finanças.

**Objetivo** principal é fornecer uma ferramenta completa para o gerenciamento financeiro pessoal, permitindo que os usuários acompanhem gastos e receitas, planejem orçamentos e tomem decisões financeiras mais conscientes e estratégicas.

---

## 🔐 API de Autenticação com Spring Boot + JWT  

Este projeto é uma **API backend** desenvolvida em **Java com Spring Boot** que implementa autenticação e autorização de usuários utilizando **JWT (JSON Web Token)**.  

O objetivo é fornecer uma estrutura segura para login e controle de acesso a endpoints protegidos, sem depender de provedores externos (ex: Auth0), totalmente integrada a um banco de dados **PostgreSQL**.  

---

## ⚙️ Tecnologias Utilizadas
- **Java 17+**  
- **Spring Boot 3**  
- **Spring Security 6**  
- **Spring Data JPA**  
- **PostgreSQL**  
- **JWT (JSON Web Token)**  

---

## 🔑 Fluxo de Autenticação
1. O usuário envia **e-mail e senha** para o endpoint de login.  
2. A API valida a senha com `PasswordEncoder`.  
3. Se válido, é gerado um **token JWT** com tempo de expiração.  
4. O token deve ser enviado em cada requisição no **header Authorization**.  
5. Um **filtro JWT customizado** (`JwtAuthenticationFilter`) valida o token e autentica o usuário no contexto do Spring Security.  
6. Endpoints protegidos só são acessíveis com token válido.  

---

## 📂 Estrutura do Projeto
- **Controller** → recebe requisições de login e demais endpoints.  
- **Service** → lógica de autenticação e geração/validação de tokens.  
- **Repository** → interface para acessar o banco (ex: `findByEmail`).  
- **Entity** → mapeamento JPA para os usuários.  
- **Configuração de Segurança** → `SecurityConfig` define as regras de acesso e aplica o filtro JWT.  

---

## 🔁 Funcionalidades Principais
### **DESPESA**
- **Salvar:** Registra novos gastos.
- **Editar:** Alterar informações de uma nova despesa.
- **Deletar:** Remover depesas registradas.
- **Listar:** Exibir todas as despesas cadastradas.
- **Calcular gasto por categoria no mês:** Analisar os gastos mensais por categoria.
- **Calcular total gasto no mês:** Somar todas as despesas do mês.
- **Listar despesas no mês:** Filtrar despesas por período mensal.
- **Listar despesas por categoria:** Filtrar despesas por categoria específica.
- **Processar parcelas recorrentes:** Gerenciar despesas parceladas ou recorrentes automaticamente.

### **RECEITA**
- **Salvar receita:** Registrar entradas financeiras.
- **Editar receita:** Alterar informações de uma receita existente.
- **Deletar receita:** Remover receitas registradas.
- **Listar todas as receitas:** Exibir todas as receitas cadastradas.

### **ORÇAMENTO**
- **Salvar orçamento:** Registrar orçamentos por categoria.
- **Editar orçamento:** Alterar valores ou categorias de um orçamento existente.
- **Deletar orçamento:** Remover orçamentos cadastrados.
- **Calcular orçamento por categoria:** Analisar quanto foi planejado e quanto foi gasto em cada categoria.
- **Saldo restante do orçamento:** Calcular quanto ainda pode ser gasto por categoria.

### **CATEGORIAS**
- **Salvar categoria:** Criar novas categorias de despesas ou receitas.
- **Listar todas as categorias por usuário:** Visualizar categorias específicas de cada usuário.
- **Editar categoria:** Alterar informações de uma categoria existente.
- **Deletar categoria:** Remover categorias cadastradas.

### **USUÁRIOS**
- **Salvar usuário:** Registrar novos usuários na plataforma.
- **Buscar usuário por ID:** Consultar dados de um usuário específico.
- **Listar todos os usuários:** Exibir todos os usuários cadastrados.
- **Editar usuário existente:** Atualizar informações de um usuário.
- **Autenticação login:** Permitir login seguro.
- **Verificar se o email já foi cadastrado:** Evitar duplicidade de usuários.

---
