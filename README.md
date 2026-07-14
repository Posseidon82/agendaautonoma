# Agenda Autônoma - API de Agendamento

## 📋 Sobre o Projeto

API para profissionais autônomos gerenciarem sua agenda de atendimentos. Clientes podem consultar horários disponíveis e agendar compromissos de forma autônoma, sem necessidade de troca de mensagens. O sistema previne conflitos de horário, gerencia notificações por e-mail (simuladas) e mantém histórico completo de agendamentos.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.1 | Framework principal (Web, Data JPA, Security, Mail, Validation) |
| Spring Security | 6.2.1 | Autenticação e autorização com JWT |
| JWT (jjwt) | 0.11.5 | Tokens de autenticação |
| H2 Database | 2.2.224 | Banco em memória para desenvolvimento |
| PostgreSQL | (opcional) | Banco para produção |
| Lombok | 1.18.30 | Redução de boilerplate |
| Maven | 3.8+ | Gerenciamento de dependências e build |
| JUnit 5 | 5.10.1 | Testes unitários |
| Mockito | 5.7.0 | Mocks para testes |
| Spring Boot Test | 3.2.1 | Testes de integração |
| OpenAPI (Springdoc) | 2.3.0 | Documentação interativa da API |

---

## 🚀 Como Executar a Aplicação

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.8+
- (Opcional) Docker e Docker Compose para rodar PostgreSQL

### Passo a Passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/agenda-autonoma.git
   cd agenda-autonoma
