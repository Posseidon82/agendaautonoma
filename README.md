# Agenda Autônoma - API de Agendamento

## 📋 Sobre o Projeto

API para profissionais autônomos gerenciarem sua agenda de atendimentos. Clientes podem consultar horários disponíveis e agendar compromissos de forma autônoma, sem necessidade de troca de mensagens. O sistema previne conflitos de horário, gerencia notificações por e-mail (simuladas) e mantém histórico completo de agendamentos.

---

## 📁 Estrutura do Projeto

<p>src/main/java/com/agendaautonoma/<br>
├── domain/                 # Entidades e enums<br>
├── application/            # Serviços (casos de uso)<br>
├── infrastructure/         # Repositórios, segurança, e-mail, configurações<br>
└── interfaces/             # Controllers, DTOs, assemblers</p>

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
2. **Compile o projeto e execute os testes (opcional)**
   ```bash
   mvn clean verify
3. **Inicie a aplicação**
   ```bash
   mvn spring-boot:run
   ```
   A aplicação sobe na porta ``8080`` com banco H2 em memória.
   Dois usuários de teste são criados automaticamente (veja credenciais abaixo).
   
4. **Acesse a documentação interativa da API**
   * Swagger UI: <http://localhost:8080/swagger-ui.html>
   * Console H2: <http://localhost:8080/h2-console>
   * JDBC URL: jdbc:h2:mem:agendaautonoma
   * Usuário: sa | Senha: (deixe em branco)

### Credenciais Iniciais (para testes)
   | Papel | E-mail | Senha |
   |-------|--------|-------|
   | Profissional | ``prof@email.com`` | ``123456`` |
   | Cliente | ``cliente@email.com`` | ``123456`` |

### Configuração para PostgreSQL (Produção)

   Caso queira utilizar PostgreSQL, edite ``src/main/resources/application.properties``:
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/agenda
   spring.datasource.username=postgres
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
   ```
   E descomente a dependência do PostgreSQL no ``pom.xml`` (ou adicione):

   ```bash
   <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
   </dependency>
   ```

   Crie o banco ``agenda`` antes de iniciar.

---

## 🧪 Como Rodar os Testes

### Testes Unitários

Executam validações de regras de negócio (cálculo de slots, conflitos, etc.) de forma isolada:
```bash
mvn test
```

### Testes de Integração

Verificam endpoints, interações com banco de dados e fluxos completos:
```bash
mvn verify -DskipTests=false
```

### Relatório de Cobertura (Jacoco)

Gere o relatório de cobertura de testes:
```bash
mvn clean test jacoco:report
```
O relatório estará disponível em ``target/site/jacoco/index.html``.

---

## 🏗️ Arquitetura e Evolução para Microsserviços

O projeto foi concebido como monolito modular com separação clara de responsabilidades, facilitando uma futura migração para microsserviços:

* Módulo de Usuários – gerenciamento de cadastro, login e horários.
* Módulo de Agendamentos – lógica de criação, cancelamento e reagendamento.
* Módulo de Notificações – envio de e-mails e gerenciamento de filas.

Atualmente, todos os módulos residem no mesmo projeto e compartilham o mesmo banco de dados. Para evoluir para microsserviços:

1. Extraia cada módulo para um projeto Spring Boot separado.
2. Utilize Service Discovery (Eureka) e API Gateway para roteamento.
3. Comunicação síncrona via REST/OpenFeign ou assíncrona via RabbitMQ.
4. Cada serviço terá seu próprio banco de dados (ou schema isolado).

O código atual já emprega injeção de dependência e interfaces que permitem essa separação.

---

## 📊 Diagramas

### Diagrama de Entidades:

   ```mermaid
     erDiagram
       USUARIO {
           Long id
           String nome
           String email
           String telefone
           String senha
           String papel
           String diasTrabalho
           LocalTime inicioTrabalho
           LocalTime fimTrabalho
           Integer duracaoPadraoMinutos
       }
       AGENDAMENTO {
           Long id
           Long profissionalId
           Long clienteId
           String clienteNome
           String clienteEmail
           LocalDate data
           LocalTime horaInicio
           LocalTime horaFim
           String status
           LocalDateTime criadoEm
           LocalDateTime atualizadoEm
       }
       NOTIFICACAO {
           Long id
           String tipo
           String destinatarioEmail
           String conteudo
           LocalDateTime dataEnvio
           Long agendamentoId
           boolean enviada
       }
   
       USUARIO ||--o{ AGENDAMENTO : "profissional"
       USUARIO ||--o{ AGENDAMENTO : "cliente"
       AGENDAMENTO ||--o{ NOTIFICACAO : "gerada"
   ```

### Diagrama de Sequência (criação de agendamento):

   ```mermaid
      sequenceDiagram
          actor Cliente
          participant Controller as AgendamentoController
          participant Service as AgendamentoService
          participant Validator as ConflitoValidator
          participant Notif as NotificacaoService
          participant DB as Banco
      
          Cliente->>Controller: POST /agendamentos (dados)
          Controller->>Service: criarAgendamento(dto)
          Service->>Validator: existeConflito(profId, data, inicio, fim)
          Validator->>DB: findByProfissionalIdAndDataAndStatus
          DB-->>Validator: lista
          Validator-->>Service: false (sem conflito)
          Service->>DB: save(Agendamento)
          DB-->>Service: Agendamento salvo
          Service->>Notif: enviar(notificacao, "email")
          Notif-->>Service: OK
          Service-->>Controller: Agendamento criado
          Controller-->>Cliente: 201 Created
   ```

### Diagrama de Arquitetura:

   ```mermaid
      graph TD
          UI[Cliente/Profissional] --> API[API REST - Spring Boot]
          API --> Controllers[Controllers]
          Controllers --> Services[Services - Camada de Aplicação]
          Services --> Domain[Domínio]
          Services --> Repositories[Repositories JPA]
          Repositories --> DB[(H2/PostgreSQL)]
          Services --> Mail[Simulador de E-mail]
   ```

### ADRs (Architecture Decision Records):

ADR 001: Monolito inicial

    Contexto: necessidade de entrega rápida e time pequeno.
    Decisão: iniciar como monolito modular, com pacotes bem definidos.
    Consequências: facilidade de desenvolvimento, deploy único; futura migração para microsserviços viável via extração de módulos.

ADR 002: JWT para autenticação

    Contexto: API stateless, necessidade de segurança.
    Decisão: usar JWT com Spring Security.
    Consequências: escalabilidade, sem sessões no servidor; tokens curtos.

ADR 003: Notificações assíncronas

    Contexto: envio de e-mail não deve atrasar resposta da API.
    Decisão: usar @Async ou fila em memória (futuro: RabbitMQ).
    Consequências: melhor performance; complexidade adicional de gerenciamento.

---

## 📝 Licença

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais informações.

---

## 📧 Contato

Em caso de dúvidas, entre em contato pelo e-mail: agendaautonoma@hotmail.com
