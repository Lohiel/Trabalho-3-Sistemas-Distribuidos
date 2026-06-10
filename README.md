# Sistema de Hotelaria Distribuído

Implementa uma arquitetura distribuída com **API REST**, **comunicação indireta via RabbitMQ** e **armazenamento em memória**. Os clientes são desenvolvidos em **Python** e **JavaScript**, linguagens distintas do servidor em **Java Spring Boot**.

---

## Arquitetura

```
Cliente Python (CLI)
        |
Cliente JavaScript (Web)
        |
        v
Spring Boot REST API  (porta 8080)
        |
        v
RabbitMQ — Direct Exchange: hotel.reserva.direct
        |         Routing Key: reserva.evento
        |
        +-----------------------------+
        |                             |
        v                             v
ReservasConsumer             NotificacaoConsumer
(queue.reserva.processar)    (queue.reserva.notificar)
        |
        v
In-Memory Storage (ConcurrentHashMap)
   ├── Clientes
   ├── Hospedagens
   └── Reservas
```

---

## Objetos Distribuídos

| Objeto | Responsabilidade |
|---|---|
| **Cliente** | Representa o hóspede (`id`, `nome`, `cpf`, `telefone`) |
| **Hospedagem** | Representa o meio de hospedagem (`id`, `nome`, `tipo`, `cidade`, `estado`, `diariaBase`, `disponivel`) |
| **Reserva** | Registra a reserva (`id`, `cliente`, `hospedagem`, `dataEntrada`, `dataSaida`, `valorTotal`, `status`) |

### Status da Reserva (`StatusReserva`)
```java
CRIADA → PROCESSADA → EFETIVADA
                  ↘ CANCELADA
```

---

## Conceitos de Sistemas Distribuídos Demonstrados

| Conceito | Como é demonstrado |
|---|---|
| **Desacoplamento espacial** | Os clientes desconhecem os consumidores; toda comunicação passa pela API REST |
| **Desacoplamento temporal** | A API responde imediatamente; os consumidores processam em background |
| **Comunicação assíncrona** | Mensagens são publicadas no RabbitMQ e consumidas de forma independente |
| **Persistência de mensagens** | As filas são `durable=true`; mensagens sobrevivem a reinicializações do broker |
| **Tolerância a falhas** | Com consumidores desabilitados, mensagens ficam na fila e são processadas ao reativar |

---

## Endpoints da API REST

Documentação interativa: **http://localhost:8080/swagger-ui/index.html**

| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/api/clientes` | Listar clientes | 200 OK |
| POST | `/api/clientes` | Cadastrar cliente | 201 Created |
| GET | `/api/hospedagens` | Listar hospedagens | 200 OK |
| POST | `/api/hospedagens` | Cadastrar hospedagem | 201 Created |
| GET | `/api/reservas` | Listar reservas | 200 OK |
| GET | `/api/reservas/{id}` | Buscar reserva por ID | 200 OK |
| POST | `/api/reservas` | Solicitar reserva (publica no RabbitMQ) | 201 Created |
| PUT | `/api/reservas/{id}/cancelar` | Cancelar reserva (publica no RabbitMQ) | 200 OK |
| PUT | `/api/reservas/{id}/efetivar` | Efetivar reserva (publica no RabbitMQ) | 200 OK |

---

## Pré-requisitos

| Componente | Versão mínima |
|---|---|
| Java JDK | 17+ |
| Maven | 3.8+ |
| RabbitMQ | 3.x |
| Node.js | 18+ (para cliente JS) |
| Python | 3.8+ (para cliente Python) |

### Instalar dependência do cliente Python
```bash
cd Projeto-Cliente-Python
pip install -r requirements.txt
```

### Instalar RabbitMQ (Ubuntu/Debian)
```bash
sudo apt-get install rabbitmq-server
sudo systemctl start rabbitmq-server
sudo rabbitmq-plugins enable rabbitmq_management
```

---

## Como Executar

### 1. Iniciar o RabbitMQ
```bash
sudo systemctl start rabbitmq-server
# Painel de gerenciamento: http://localhost:15672 (guest/guest)
```

### 2. Iniciar o Servidor Spring Boot
```bash
cd Projeto-Servidor
./mvnw spring-boot:run
```
O servidor inicia na porta `8080` e popula automaticamente o In-Memory Storage com:
- **Clientes**: João Silva, Maria Souza, Carlos Lima
- **Hospedagens**: Hotel Central, Pousada Serra, Motel Premium

### 3. Iniciar o Cliente JavaScript
```bash
cd Projeto-Cliente-JavaScript
npm run dev
# Acesse: http://localhost:3000
```

### 4. Iniciar o Cliente Python
```bash
cd Projeto-Cliente-Python
python3 client.py
```

---

## Cenários de Demonstração

### Cenário 1 — Fluxo Normal (Happy Path)

1. Abra o Cliente Python ou JavaScript
2. Solicite uma reserva (ex.: Cliente ID 1, Hospedagem ID 2)
3. A API retorna imediatamente com `status: CRIADA`
4. Nos logs do servidor, observe:
   - `ReservasConsumer` processa a mensagem → atualiza status para `PROCESSADA` e marca hospedagem como indisponível
   - `NotificacaoConsumer` processa em paralelo → exibe simulação de e-mail/SMS
5. Atualize a lista de reservas: status agora é `PROCESSADA`

### Cenário 2 — Tolerância a Falhas e Desacoplamento Temporal

**Passo 1** — Iniciar RabbitMQ normalmente

**Passo 2** — Iniciar a API com consumidores desabilitados:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--app.consumers.enabled=false"
```

**Passo 3** — Solicitar uma reserva pelo cliente  
→ Resultado: `status = CRIADA` (API responde normalmente)

**Passo 4** — Verificar o painel RabbitMQ (http://localhost:15672)  
→ Mensagens aguardando nas filas `queue.reserva.processar` e `queue.reserva.notificar`  
→ Status no In-Memory Storage permanece `CRIADA`

**Passo 5** — Parar o servidor e reiniciar com consumidores habilitados:
```bash
./mvnw spring-boot:run
```

**Passo 6** — Verificar o processamento automático nos logs  
→ Resultado: `status = PROCESSADA`

Este cenário demonstra:
- ✅ Desacoplamento espacial
- ✅ Desacoplamento temporal
- ✅ Comunicação assíncrona
- ✅ Persistência de mensagens no broker
- ✅ Tolerância a falhas

---

## Estrutura do Projeto

```
trabalho 3 - projeto hotelaria/
├── README.md
├── Projeto-Servidor/
│   ├── pom.xml
│   └── src/main/java/com/hotelaria/
│       ├── Application.java
│       ├── config/
│       │   ├── RabbitMQConfig.java
│       │   └── SwaggerConfig.java
│       ├── controller/
│       │   ├── ClienteController.java
│       │   ├── HospedagemController.java
│       │   ├── ReservaController.java
│       │   └── GlobalExceptionHandler.java
│       ├── dto/
│       │   ├── ClienteDTO.java
│       │   ├── HospedagemDTO.java
│       │   ├── ReservaDTO.java
│       │   ├── ReservaRequestDTO.java
│       │   └── ReservaEventMessage.java
│       ├── model/
│       │   ├── Cliente.java
│       │   ├── Hospedagem.java
│       │   ├── Reserva.java
│       │   └── StatusReserva.java
│       ├── repository/
│       │   ├── ClienteRepository.java
│       │   ├── HospedagemRepository.java
│       │   └── ReservaRepository.java
│       ├── service/
│       │   └── HotelariaService.java
│       ├── messaging/
│       │   ├── producer/
│       │   │   └── ReservaProducer.java
│       │   └── consumer/
│       │       ├── ReservasConsumer.java
│       │       └── NotificacaoConsumer.java
│       └── seed/
│           └── DatabaseSeeder.java
├── Projeto-Cliente-Python/
│   ├── client.py
│   └── requirements.txt
└── Projeto-Cliente-JavaScript/
    ├── index.html
    ├── style.css
    ├── app.js
    ├── server.js
    └── package.json
```
