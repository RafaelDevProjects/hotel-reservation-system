# 🏨 Sistema de Reserva de Hotel

## 📋 Descrição
Sistema completo de gerenciamento de reservas de hotel desenvolvido com Java Spring Boot, seguindo arquitetura MVC e boas práticas REST. O sistema permite o cadastro de quartos, gestão de reservas, check-in, check-out e validações de negócio.

## 🛠 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (banco em memória)
- **Flyway** (migração de banco)
- **SpringDoc OpenAPI 3.0** (documentação Swagger)
- **Maven**
- **Jakarta Validation**

## 🚀 Funcionalidades

### Gestão de Quartos
- Cadastro de quartos com tipo, capacidade e valor da diária
- Listagem de quartos disponíveis
- Busca por ID e status
- Desativação de quartos

### Gestão de Reservas
- Criação de reservas com validação de datas
- Check-in e check-out
- Cancelamento de reservas
- Cálculo automático de valores
- Validação de disponibilidade do quarto

### Validações e Regras de Negócio
- Validação de datas (check-out deve ser após check-in)
- Verificação de disponibilidade do quarto
- Controle de fluxo de status da reserva
- Tratamento de exceções global

## 🏗 Arquitetura
O sistema segue o padrão MVC com separação clara de responsabilidades:

- **Controller**: Camada de apresentação, endpoints REST
- **Service**: Camada de negócio, regras e validações
- **Repository**: Camada de acesso a dados
- **DTO**: Objetos de transferência de dados
- **Model**: Entidades JPA
- **Exception**: Tratamento global de exceções

## 📥 Instalação e Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior

### Execução
Clone o repositório e execute:
mvn spring-boot:run

A aplicação estará disponível em: http://localhost:8080

## 📚 Documentação da API

### Swagger UI
Acesse a documentação interativa completa em: 
http://localhost:8080/swagger-ui.html

### Endpoints Disponíveis

#### Quartos (/api/rooms)
- GET /api/rooms - Listar todos os quartos
- GET /api/rooms/{id} - Buscar quarto por ID
- GET /api/rooms/status/{status} - Buscar quartos por status
- POST /api/rooms - Criar novo quarto
- PATCH /api/rooms/{id}/deactivate - Desativar quarto

#### Reservas (/api/reservations)
- GET /api/reservations - Listar todas as reservas
- GET /api/reservations/{id} - Buscar reserva por ID
- GET /api/reservations/status/{status} - Buscar reservas por status
- POST /api/reservations - Criar nova reserva
- PATCH /api/reservations/{id}/checkin - Realizar check-in
- PATCH /api/reservations/{id}/checkout - Realizar check-out
- PATCH /api/reservations/{id}/cancel - Cancelar reserva

## 📊 Banco de Dados

### Console H2
Acesse o console do banco H2 em:
http://localhost:8080/h2-console

Credenciais:
- JDBC URL: jdbc:h2:mem:hoteldb
- User Name: sa
- Password: (deixe em branco)

### Estrutura das Tabelas

#### Tabela: rooms
- id (UUID)
- number (INT, único)
- type (VARCHAR)
- capacity (INT)
- price_per_night (DECIMAL)
- status (VARCHAR)

#### Tabela: reservations
- id (UUID)
- room_id (FK para rooms)
- guest_name (VARCHAR)
- checkin_expected (DATE)
- checkout_expected (DATE)
- status (VARCHAR)
- total_amount (DECIMAL)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## ⚙️ Regras de Negócio

### Validação de Datas
- Check-out deve ser após check-in
- Datas devem ser futuras ou presentes
- Retorna HTTP 400 em caso de datas inválidas

### Disponibilidade do Quarto
- Não permite reservas com datas sobrepostas
- Valida conflitos para status CREATED e CHECKED_IN
- Retorna HTTP 409 em caso de conflito

### Fluxo de Status da Reserva
CREATED → CHECKED_IN → CHECKED_OUT
CREATED → CANCELED

Apenas transições permitidas, retorna HTTP 409 para transição inválida.

### Cálculo de Valores
- Calculado automaticamente no check-out
- Baseado no número de diárias
- Usa preço do quarto na data da reserva

## 🧪 Testando a API

### Via Swagger UI
1. Acesse http://localhost:8080/swagger-ui.html
2. Expanda os endpoints desejados
3. Use o botão "Try it out" para testar
4. Preencha os parâmetros necessários
5. Execute e veja a resposta

### Fluxo de Teste Recomendado
1. Listar quartos disponíveis
2. Criar uma reserva
3. Realizar check-in
4. Realizar check-out
5. Verificar cálculo automático do valor

## 📁 Estrutura do Projeto
```
src/main/java/com/hotel/
├── controller/
│   ├── ReservationController.java
│   └── RoomController.java
├── dto/
│   ├── ReservationRequestDTO.java
│   ├── ReservationResponseDTO.java
│   ├── RoomRequestDTO.java
│   └── RoomResponseDTO.java
├── exception/
│   ├── BusinessException.java
│   ├── ConflictException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   ├── Reservation.java
│   └── Room.java
├── repository/
│   ├── ReservationRepository.java
│   └── RoomRepository.java
├── service/
│   ├── ReservationService.java
│   └── RoomService.java
└── HotelReservationSystemApplication.java

src/main/resources/
├── db/migration/
│   ├── V1__Create_tables.sql
│   └── V2__Insert_initial_data.sql
└── application.properties
```
## 🐛 Tratamento de Erros

O sistema possui tratamento global de exceções com respostas padronizadas:

- 400 Bad Request: Dados inválidos
- 404 Not Found: Recurso não encontrado
- 409 Conflict: Conflito de regras de negócio
- 500 Internal Server Error: Erro interno do servidor

---

**Desenvolvido para FIAP - 3ESPR 2025**
