# Para Onde Foi o Meu Dinheiro? (PODNV)

# 🔐 API de Autenticação com Spring Boot + JWT  

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


