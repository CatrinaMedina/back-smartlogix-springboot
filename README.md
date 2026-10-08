# 🚚 SmartLogix Backend

Backend de **SmartLogix**, plataforma de gestión logística desarrollada bajo una arquitectura de **microservicios con Spring Boot**.

El sistema integra autenticación mediante JWT, descubrimiento de servicios con Eureka, enrutamiento mediante API Gateway, gestión de usuarios, inventario, pedidos, envíos y notificaciones.

La comunicación entre los servicios se complementa con **OpenFeign** y **RabbitMQ**, mientras que cada servicio mantiene su propia base de datos PostgreSQL.

---

## 🚀 Tecnologías

- Java 21
- Spring Boot 4.0.5
- Spring Cloud 2025.1.1
- Spring Security
- Spring Cloud Gateway
- Netflix Eureka
- Spring Data JPA
- OpenFeign
- JWT / JJWT 0.12.6
- PostgreSQL 16
- RabbitMQ
- Docker
- Docker Compose
- Maven
- Lombok
- SpringDoc OpenAPI / Swagger
- SonarQube

---

## 🏗️ Arquitectura

SmartLogix utiliza una arquitectura de microservicios donde cada módulo tiene una responsabilidad específica.

```text
                           FRONTEND
                              │
                              ▼
                    ┌─────────────────┐
                    │   API GATEWAY   │
                    │      :8080      │
                    │ JWT + Security  │
                    └────────┬────────┘
                             │
              ┌──────────────┼───────────────┐
              │              │               │
              ▼              ▼               ▼
        ┌──────────┐   ┌────────────┐   ┌────────────┐
        │ Usuarios │   │ Inventario │   │  Pedidos   │
        │  :8081   │   │   :8082    │   │   :8083    │
        └──────────┘   └────────────┘   └────────────┘
                                             │
                              ┌──────────────┼──────────────┐
                              ▼              ▼              ▼
                         ┌──────────┐  ┌──────────────┐  ┌───────────────┐
                         │ Envíos   │  │Notificaciones│  │   RabbitMQ    │
                         │  :8084   │  │    :8085    │  │   Messaging   │
                         └──────────┘  └──────────────┘  └───────────────┘

                              ▲
                              │
                    ┌─────────────────┐
                    │     Eureka      │
                    │      :8761      │
                    │ Service Discovery│
                    └─────────────────┘
```

---

## 📦 Microservicios

### 🔐 API Gateway — `apigateway`

Puerto:

```text
8080
```

Es el punto de entrada principal de la aplicación.

Responsabilidades:

- Enrutamiento de solicitudes.
- Validación de JWT.
- Seguridad.
- Autorización por roles.
- Comunicación con Eureka.
- Balanceo y descubrimiento de servicios.
- Configuración CORS para el frontend.

Rutas principales:

```text
/api/usuarios/**
/api/inventario/**
/api/pedidos/**
```

---

### 🌐 Eureka Server — `eurekaserver`

Puerto:

```text
8761
```

Se encarga del **Service Discovery**, permitiendo que los microservicios se registren y puedan localizarse dinámicamente.

---

### 👤 Servicio de Usuarios — `serviciousuarios`

Puerto:

```text
8081
```

Responsable de:

- Registro de usuarios.
- Inicio de sesión.
- Gestión de usuarios.
- Generación de tokens JWT.
- Validación de credenciales.
- Gestión de roles.

Roles utilizados:

```text
ADMIN
VENDEDOR
USER
```

Endpoints principales:

```http
POST /api/usuarios/registrar
POST /api/usuarios/login
GET  /api/usuarios/{username}
```

---

### 📦 Servicio de Inventario — `servicioinventario`

Puerto:

```text
8082
```

Responsable de la gestión de productos e inventario.

Incluye:

- Productos.
- Bodegas.
- Proveedores.
- Stock por bodega.
- Control de inventario.
- Alertas de stock.

Endpoints principales:

```http
POST   /api/inventario
GET    /api/inventario
GET    /api/inventario/{id}
PUT    /api/inventario/{id}
DELETE /api/inventario/{id}
```

Los endpoints de modificación están restringidos según el rol del usuario.

---

### 🛒 Servicio de Pedidos — `serviciopedidos`

Puerto:

```text
8083
```

Responsable de:

- Creación de pedidos.
- Consulta de pedidos.
- Generación de boletas.
- Validación de usuarios.
- Validación de productos.
- Validación de stock.
- Comunicación con otros microservicios mediante OpenFeign.

Endpoints principales:

```http
POST /api/pedidos
GET  /api/pedidos
GET  /api/pedidos/{id}
```

Durante la creación de un pedido se validan los datos necesarios antes de almacenarlo.

---

### 🚚 Servicio de Envíos — `servicioenvios`

Puerto:

```text
8084
```

Responsable de la gestión de envíos.

Incluye:

- Registro de envíos.
- Consulta de envíos.
- Gestión de estados.
- Comunicación con el servicio de transportista.
- Patrones Factory para la creación de diferentes tipos de envío.

Tipos contemplados:

```text
Envío normal
Envío express
```

---

### 🔔 Servicio de Notificaciones — `servicionotificaciones`

Puerto:

```text
8085
```

Responsable de las notificaciones generadas por eventos del sistema.

Incluye:

- Gestión de notificaciones.
- Servicio de correo.
- Consumo de eventos desde RabbitMQ.
- Integración con eventos provenientes del inventario.

---

## 🔐 Seguridad

La autenticación se implementa utilizando:

```text
Spring Security
JWT
```

El proceso general es:

```text
Usuario
   │
   ▼
Login
   │
   ▼
Servicio de Usuarios
   │
   ▼
JWT
   │
   ▼
Frontend
   │
   ▼
API Gateway
   │
   ▼
Validación JWT
   │
   ▼
Microservicio correspondiente
```

El token contiene información relacionada con:

- ID del usuario.
- Username.
- Rol.
- Expiración.

La configuración del API Gateway aplica autorización basada en roles.

---

## 🔄 Comunicación entre microservicios

El proyecto utiliza diferentes mecanismos de comunicación dependiendo de la necesidad.

### OpenFeign

Se utiliza para comunicación directa entre servicios.

Ejemplo:

```text
serviciopedidos
      │
      ├── servicio usuarios
      │
      └── servicio inventario
```

Esto permite validar usuarios y productos antes de procesar pedidos.

### RabbitMQ

Se utiliza para comunicación basada en eventos.

Actualmente participa principalmente en:

```text
Inventario
     │
     ▼
 RabbitMQ
     │
     ▼
Notificaciones
```

Esto permite desacoplar determinadas operaciones entre servicios.

---

## 🗄️ Bases de datos

Cada microservicio utiliza su propia base de datos.

```text
serviciousuarios       → baseusuarios
servicioinventario     → baseinventario
serviciopedidos        → basepedidos
servicioenvios         → baseenvios
servicionotificaciones → basenotificaciones
```

Las bases de datos utilizadas en Docker corresponden a **PostgreSQL 16**.

Los servicios utilizan JPA/Hibernate para la persistencia.

---

## 🐳 Docker Compose

El proyecto incluye:

```text
docker-compose.yml
```

El archivo permite levantar la infraestructura completa del backend.

Incluye:

- Eureka Server
- API Gateway
- Servicio de Usuarios
- Servicio de Inventario
- Servicio de Pedidos
- Servicio de Envíos
- Servicio de Notificaciones
- RabbitMQ
- PostgreSQL para cada servicio
- SonarQube

Puertos principales:

| Servicio | Puerto |
|---|---:|
| API Gateway | `8080` |
| Usuarios | `8081` |
| Inventario | `8082` |
| Pedidos | `8083` |
| Envíos | `8084` |
| Notificaciones | `8085` |
| Eureka | `8761` |
| RabbitMQ | `5672` |
| RabbitMQ Management | `15672` |
| SonarQube | `9000` |

Puertos externos de las bases PostgreSQL:

| Base de datos | Puerto |
|---|---:|
| Usuarios | `5433` |
| Inventario | `5434` |
| Pedidos | `5435` |
| Envíos | `5436` |
| Notificaciones | `5437` |

---

## ⚙️ Requisitos

Para ejecutar el proyecto localmente:

- Java 21
- Maven
- Docker
- Docker Compose
- Git

Para una ejecución mediante Docker, se recomienda utilizar Docker Desktop.

---

## ▶️ Ejecutar con Docker

Clonar el repositorio:

```bash
git clone https://github.com/CatrinaMedina/back-smartlogix-springboot.git
```

Entrar al proyecto:

```bash
cd back-smartlogix-springboot
```

Levantar todos los servicios:

```bash
docker compose up --build
```

Para ejecutar en segundo plano:

```bash
docker compose up -d --build
```

Ver los contenedores:

```bash
docker compose ps
```

Ver logs:

```bash
docker compose logs -f
```

Detener los servicios:

```bash
docker compose down
```

Detener y eliminar volúmenes:

```bash
docker compose down -v
```

---

## ▶️ Ejecutar microservicios individualmente

Cada servicio es un proyecto Spring Boot independiente.

### Windows

```bash
cd apigateway
mvnw.cmd spring-boot:run
```

o:

```bash
cd serviciousuarios
mvnw.cmd spring-boot:run
```

Los mismos comandos pueden utilizarse para:

```text
eurekaserver
servicioinventario
serviciopedidos
servicioenvios
servicionotificaciones
```

### Linux / macOS

```bash
cd apigateway
./mvnw spring-boot:run
```

---

## 🧪 Testing

Cada microservicio contiene pruebas dentro de:

```text
src/test/
```

Para ejecutar las pruebas de un servicio:

```bash
cd serviciousuarios
mvnw.cmd test
```

En Linux/macOS:

```bash
./mvnw test
```

El mismo procedimiento se puede aplicar a los demás microservicios.

---

## 📊 SonarQube

El proyecto incluye SonarQube mediante Docker Compose.

Disponible en:

```text
http://localhost:9000
```

SonarQube permite realizar análisis de:

- Calidad de código.
- Bugs.
- Vulnerabilidades.
- Code smells.
- Cobertura.
- Deuda técnica.

---

## 📁 Estructura del proyecto

```text
back-smartlogix-springboot/
│
├── apigateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── eurekaserver/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── servicioenvios/
│   ├── src/
│   ├── baseenvios.sql
│   ├── Dockerfile
│   └── pom.xml
│
├── servicioinventario/
│   ├── src/
│   ├── baseinventario.sql
│   ├── Dockerfile
│   └── pom.xml
│
├── servicionotificaciones/
│   ├── src/
│   ├── basenotificaciones.sql
│   ├── Dockerfile
│   └── pom.xml
│
├── serviciopedidos/
│   ├── src/
│   ├── basepedidos.sql
│   ├── Dockerfile
│   └── pom.xml
│
├── serviciousuarios/
│   ├── src/
│   ├── baseusuarios.sql
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

El `pom.xml` raíz funciona como proyecto Maven agregador e integra los siete módulos del backend. :contentReference[oaicite:1]{index=1}

---

## 🔌 Flujo principal

### Autenticación

```text
Frontend
   │
   ▼
API Gateway
   │
   ▼
Servicio de Usuarios
   │
   ▼
Validación de credenciales
   │
   ▼
JWT
   │
   ▼
Frontend
```

### Gestión de pedidos

```text
Frontend
   │
   ▼
API Gateway
   │
   ▼
Servicio de Pedidos
   │
   ├──► Servicio de Usuarios
   │
   ├──► Servicio de Inventario
   │
   └──► Servicio de Envíos
```

### Eventos y notificaciones

```text
Servicio de Inventario
          │
          ▼
       RabbitMQ
          │
          ▼
Servicio de Notificaciones
```

---

## 🌐 URLs principales

Con los servicios ejecutándose:

```text
API Gateway
http://localhost:8080
```

```text
Usuarios
http://localhost:8081
```

```text
Inventario
http://localhost:8082
```

```text
Pedidos
http://localhost:8083
```

```text
Envíos
http://localhost:8084
```

```text
Notificaciones
http://localhost:8085
```

```text
Eureka
http://localhost:8761
```

```text
RabbitMQ Management
http://localhost:15672
```

```text
SonarQube
http://localhost:9000
```

---

## 📚 Documentación API

El API Gateway incorpora **SpringDoc OpenAPI / Swagger**.

Una vez iniciado el Gateway, la documentación puede consultarse desde:

```text
http://localhost:8080/swagger-ui.html
```

y:

```text
http://localhost:8080/v3/api-docs
```

---

## 👥 Equipo

| Integrante | Área |
|---|---|
| Anaís Aravena | Servicio de Inventario |
| Catrina Corral | API Gateway y Seguridad |
| Fernanda Manríquez | Servicio de Pedidos y Documentación |

---

## 🔗 Repositorio

GitHub:

https://github.com/CatrinaMedina/back-smartlogix-springboot
