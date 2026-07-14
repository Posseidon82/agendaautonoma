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
2. **Compile o projeto e execute os testes (opcional)**
   ```bash
   mvn clean verify
3. **Inicie a aplicação**
   ```bash
   mvn spring-boot:run
   ```
   A aplicação sobe na porta 8080 com banco H2 em memória.
   Dois usuários de teste são criados automaticamente (veja credenciais abaixo).
   
4. **Acesse a documentação interativa da API**
   * Swagger UI: http://localhost:8080/swagger-ui.html
   * Console H2: http://localhost:8080/h2-console
     JDBC URL: jdbc:h2:mem:agendaautonoma
     Usuário: sa | Senha: (deixe em branco)

### Credenciais Iniciais (para testes)
   | Papel | E-mail | Senha |
   |-------|--------|-------|
   | Profissional | prof@email.com | 123456 |
   | Cliente | cliente@email.com | 123456 |

### Configuração para PostgreSQL (Produção)

   Caso queira utilizar PostgreSQL, edite src/main/resources/application.properties:
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
