# SmartLogix Backend

Backend del sistema **SmartLogix**, una plataforma de gestión logística desarrollada con arquitectura de **microservicios** en Java con Spring Boot. El sistema permite gestionar usuarios, inventario y pedidos a través de un API Gateway centralizado con autenticación JWT.

---

## Equipo de desarrollo

| Integrante | Rama de trabajo |
|---|---|
| Anaís Aravena | `feature/servicioinventario-anais` |
| Catrina Corral | `feature/apigateway-catrina` |
| Fernanda Manríquez | `feature/serviciopedidos-fer` · `feature/docs-fer` |

---

## Arquitectura general

El proyecto está compuesto por **4 microservicios independientes**, cada uno con su propia base de datos MySQL y que se comunican entre sí a través del API Gateway:

```
Frontend (localhost:5173)
        │
        ▼
┌─────────────────────┐
│    API Gateway       │  :8080
│  (Enrutamiento +     │
│   Seguridad JWT)     │
└──────┬──────┬───────┘
       │      │      │
       ▼      ▼      ▼
 Usuarios  Inventario  Pedidos
  :8081     :8082      :8083
```

---

## Microservicios

### 1. `apigateway` — Puerto 8080
Punto de entrada único para todas las peticiones del frontend. Se encarga de:
- Enrutar las solicitudes al microservicio correspondiente según el path.
- Validar el token JWT en cada petición entrante mediante un filtro (`JwtAuthFilter`).
- Gestionar CORS para el frontend en `localhost:5173`.
- Aplicar autorización por roles (ADMIN, VENDEDOR, USER).

**Rutas configuradas:**

| Path | Redirige a |
|---|---|
| `/api/usuarios/**` | `serviciousuarios` :8081 |
| `/api/inventario/**` | `servicioinventario` :8082 |
| `/api/pedidos/**` | `serviciopedidos` :8083 |

**Reglas de seguridad:**

| Endpoint | Acceso |
|---|---|
| `POST /api/usuarios/login` | Público |
| `POST /api/usuarios/registrar` | Público |
| `DELETE /api/inventario/**` | Solo `ADMIN` |
| `POST /api/inventario/**` | `ADMIN` o `VENDEDOR` |
| `PUT /api/inventario/**` | `ADMIN` o `VENDEDOR` |
| Cualquier otro | Autenticado |

---

### 2. `serviciousuarios` — Puerto 8081
Gestiona el registro, autenticación y consulta de usuarios. Genera los tokens JWT que se usan en todo el sistema.

**Endpoints:**

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| `POST` | `/api/usuarios/registrar` | Registrar nuevo usuario | Público |
| `POST` | `/api/usuarios/login` | Iniciar sesión y obtener JWT | Público |
| `GET` | `/api/usuarios/{username}` | Obtener datos de un usuario | Autenticado |

**Modelo `Usuario`:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador único |
| `username` | String | Nombre de usuario (único) |
| `password` | String | Contraseña (encriptada con BCrypt, solo escritura) |
| `correo` | String | Correo electrónico (único) |
| `rol` | String | Rol del usuario (`ADMIN`, `VENDEDOR`, `USER`) |

**Validaciones de registro:**
- `username` obligatorio.
- `password` con mínimo 8 caracteres.
- `correo` obligatorio y debe ser de dominio `@gmail.com`, `@duocuc.cl` o `@hotmail.com`.
- Si no se especifica rol, se asigna `USER` por defecto.

---

### 3. `servicioinventario` — Puerto 8082
Gestiona el catálogo de productos del sistema.

**Endpoints:**

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| `POST` | `/api/inventario` | Crear producto | `ADMIN` / `VENDEDOR` |
| `GET` | `/api/inventario` | Listar todos los productos | Autenticado |
| `GET` | `/api/inventario/{id}` | Obtener producto por ID | Autenticado |
| `PUT` | `/api/inventario/{id}` | Actualizar producto | `ADMIN` / `VENDEDOR` |
| `DELETE` | `/api/inventario/{id}` | Eliminar producto | Solo `ADMIN` |

**Modelo `Producto`:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador único |
| `nombre` | String | Nombre del producto (debe ser único) |
| `descripcion` | String | Descripción del producto |
| `cantidad` | int | Stock disponible |
| `precio` | double | Precio unitario |

---

### 4. `serviciopedidos` — Puerto 8083
Gestiona la creación y consulta de pedidos. Se comunica con `serviciousuarios` y `servicioinventario` usando **OpenFeign** para validar datos antes de crear un pedido.

**Endpoints:**

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| `POST` | `/api/pedidos` | Crear pedido | Autenticado |
| `GET` | `/api/pedidos` | Listar todos los pedidos | Autenticado |
| `GET` | `/api/pedidos/{id}` | Obtener pedido por ID | Autenticado |

**Modelo `Pedido`:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador único |
| `username` | String | Username del usuario que realiza el pedido |
| `productoId` | Long | ID del producto solicitado |
| `cantidad` | Integer | Cantidad solicitada |
| `estado` | String | Estado del pedido (`CREADO`) |

**Lógica de creación de pedido:**
1. Verifica que el usuario existe consultando `serviciousuarios`.
2. Verifica que el producto existe consultando `servicioinventario`.
3. Verifica que hay stock suficiente.
4. Guarda el pedido con estado `CREADO`.

---

## Seguridad — JWT

Todos los microservicios comparten el mismo secreto JWT. El token se genera en el login e incluye:
- `id` del usuario
- `username`
- `rol`
- Expiración de **24 horas** (86400000 ms)

El API Gateway intercepta cada request, valida el token y propaga la identidad del usuario al microservicio destino.

---

## Bases de datos

Cada microservicio tiene su propia base de datos MySQL:

| Microservicio | Base de datos |
|---|---|
| `serviciousuarios` | `baseusuarios` |
| `servicioinventario` | `baseinventario` |
| `serviciopedidos` | `basepedidos` |

Las tablas se crean y actualizan automáticamente con `spring.jpa.hibernate.ddl-auto=update`.

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.5 | Framework base |
| Spring Cloud Gateway | 2025.1.1 | API Gateway y enrutamiento |
| Spring Security | — | Autenticación y autorización |
| Spring Data JPA | — | Persistencia de datos |
| OpenFeign | — | Comunicación entre microservicios |
| jjwt (JJWT) | 0.12.6 | Generación y validación de tokens JWT |
| MySQL | — | Base de datos relacional |
| Lombok | — | Reducción de código boilerplate |
| Maven | — | Gestión de dependencias y build |

---

## Configuración y ejecución local

### Prerrequisitos
- Java 21
- Maven
- MySQL corriendo en `localhost:3306`

### 1. Crear las bases de datos en MySQL

```sql
CREATE DATABASE baseusuarios;
CREATE DATABASE baseinventario;
CREATE DATABASE basepedidos;
```

### 2. Configurar credenciales

En cada `application.properties` puedes definir las credenciales mediante variables de entorno o directamente:

```properties
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

O usando variables de entorno:
```
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=tu_password
```

### 3. Levantar los microservicios

Cada microservicio se levanta de forma independiente. Ejecutar en este orden:

```bash
# 1. Servicio de Usuarios
cd proyectoSmartLogix/serviciousuarios
./mvnw spring-boot:run

# 2. Servicio de Inventario
cd proyectoSmartLogix/servicioinventario
./mvnw spring-boot:run

# 3. Servicio de Pedidos
cd proyectoSmartLogix/serviciopedidos
./mvnw spring-boot:run

# 4. API Gateway (último)
cd proyectoSmartLogix/apigateway
./mvnw spring-boot:run
```

### 4. Verificar que todo esté corriendo

| Servicio | URL base |
|---|---|
| API Gateway | http://localhost:8080 |
| Usuarios | http://localhost:8081 |
| Inventario | http://localhost:8082 |
| Pedidos | http://localhost:8083 |

---

## Repositorio

**GitHub:** https://github.com/anaqueso/smartlogix_backend_aravena_corral_manriquez

### Ramas del repositorio

| Rama | Descripción |
|---|---|
| `main` | Rama principal con el código integrado |
| `feature/apigateway-catrina` | Desarrollo del API Gateway — **Catrina Corral** |
| `feature/servicioinventario-anais` | Desarrollo del Servicio de Inventario — **Anaís Aravena** |
| `feature/serviciopedidos-fer` | Desarrollo del Servicio de Pedidos — **Fernanda Manríquez** |
| `feature/docs-fer` | Documentación del proyecto — **Fernanda Manríquez** |
| `feature/apigateway` | Rama base del API Gateway |
| `feature/serviciousuarios-email` | Rama del Servicio de Usuarios con validación de correo |

---

## Próximas entregas

### Servicio de Notificaciones (en desarrollo)
Para la siguiente entrega se incorporará el microservicio `servicionotificaciones`, el cual permitirá enviar notificaciones automáticas a los usuarios ante eventos del sistema (creación de pedidos, cambios de estado, alertas de stock, etc.).

---

## Integrantes

| Nombre | Área principal |
|---|---|
| **Anaís Aravena** | Servicio de Inventario (`servicioinventario`) |
| **Catrina Corral** | API Gateway y Seguridad (`apigateway`) |
| **Fernanda Manríquez** | Servicio de Pedidos y Documentación (`serviciopedidos`) |
