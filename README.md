# Laboratorio 13: Service Discovery y API Gateway
## Tecsup - Desarrollo de Aplicaciones Web Avanzado

Este repositorio contiene la implementación completa del Laboratorio 13, integrando el registro y descubrimiento de servicios (**Spring Cloud Netflix Eureka**) con un punto de entrada unificado (**Spring Cloud Gateway**).

---

## 🏛️ Arquitectura del Sistema

```
                            [ Cliente Web / Postman / Navegador ]
                                             |
                                             v
                      +---------------------------------------------+
                      |        API GATEWAY (Puerto 8080)            |
                      |        Spring Cloud Gateway                 |
                      +----------------------+----------------------+
                                             |
                   +-------------------------+-------------------------+
                   |                                                   |
                   v                                                   v
  +---------------------------------+                 +---------------------------------+
  |   Eureka Server (Puerto 8761)   |                 |         Balanceador (lb)        |
  |  Service Registry & Discovery   |                 |   Spring Cloud LoadBalancer     |
  +---------------------------------+                 +---------------------------------+
                   ^                                                   |
                   |                  +----------------+---------------+----------------+
                   |                  |                |               |                |
                   |                  v                v               v                v
                   |           +--------------+ +--------------+ +-------------+ +--------------+
                   +-----------|  categoria   | |   producto   | |   cliente   | |    pedido    |
                               |   service    | |   service    | |   service   | |   service    |
                               | (Port 8082)  | | (Port 8083)  | | (Port 8084) | | (Port 8085)  |
                               +--------------+ +--------------+ +-------------+ +--------------+
                                      |                |               |                |
                                  (MySQL DB)       (MySQL DB)      (MySQL DB)       (MySQL DB)
                                bd_categoria_dev  bd_producto_dev bd_cliente_dev   bd_pedido_dev
```

---

## 📦 Componentes y Rutas en API Gateway

| Servicio | Puerto Interno | Ruta Predicate en Gateway | URI Dynamic Route | Descripción |
| :--- | :---: | :--- | :--- | :--- |
| **`eureka-server`** | `8761` | — | — | Servidor central de registro y descubrimiento. |
| **`gateway-service`** | `8080` | `/*` | — | Puerta de enlace unificada (Punto único de acceso). |
| **`categoria-service`** | `8082` | `/api/categorias/**` | `lb://categoria-service` | Microservicio de gestión de categorías. |
| **`producto-service`** | `8083` | `/api/productos/**` | `lb://producto-service` | Microservicio de productos. |
| **`cliente-service`** | `8084` | `/api/clientes/**` | `lb://cliente-service` | Microservicio de clientes (**Actividad**). |
| **`pedido-service`** | `8085` | `/api/pedidos/**` | `lb://pedido-service` | Microservicio de pedidos (**Actividad**). |

---

## ⚙️ Configuración del Gateway (`application.yaml`)

```yaml
server:
  port: 8080

spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: categoria-service
          uri: lb://categoria-service
          predicates:
            - Path=/api/categorias/**

        - id: producto-service
          uri: lb://producto-service
          predicates:
            - Path=/api/productos/**

        - id: cliente-service
          uri: lb://cliente-service
          predicates:
            - Path=/api/clientes/**

        - id: pedido-service
          uri: lb://pedido-service
          predicates:
            - Path=/api/pedidos/**

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.cloud.loadbalancer: DEBUG

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---

## 🚀 Guía de Ejecución

### 1. Iniciar Base de Datos (MySQL)
- Abrir **XAMPP Control Panel** e iniciar el servicio **MySQL** (puerto 3306).
- Verificar que las bases de datos existan:
  ```sql
  CREATE DATABASE IF NOT EXISTS bd_categoria_dev;
  CREATE DATABASE IF NOT EXISTS bd_producto_dev;
  CREATE DATABASE IF NOT EXISTS bd_cliente_dev;
  CREATE DATABASE IF NOT EXISTS bd_pedido_dev;
  ```

### 2. Iniciar Todos los Servicios
Hacer doble clic en:
```bat
iniciar-servicios.bat
```
Este script levantará automáticamente:
1. `Eureka Server` (8761)
2. `categoria-service` (8082)
3. `producto-service` (8083)
4. `cliente-service` (8084)
5. `pedido-service` (8085)
6. `gateway-service` (8080)

### 3. Detener los Servicios
Para liberar los puertos cuando termines:
```bat
detener-servicios.bat
```

---

## 📸 Guía para Captura de Evidencias

Para completar el informe de laboratorio, toma las siguientes capturas:

### Evidencia 1: Dashboard de Eureka
- Abrir en el navegador: `http://localhost:8761`
- **Captura:** Debe visualizarse la tabla **"Instances currently registered with Eureka"** con las 5 instancias:
  - `CATEGORIA-SERVICE`
  - `CLIENTE-SERVICE`
  - `GATEWAY-SERVICE`
  - `PEDIDO-SERVICE`
  - `PRODUCTO-SERVICE`

### Evidencia 2: Código del `application.yaml` del Gateway
- Abrir `gateway-service/src/main/resources/application.yaml`.
- **Captura:** Donde se muestren las rutas configuradas (`categoria-service`, `producto-service`, `cliente-service` y `pedido-service`).

### Evidencia 3: Consumo de Categorías vía Gateway
- Navegador o Postman:
  - **GET** `http://localhost:8080/api/categorias`
- **Captura:** Respuesta JSON (código HTTP 200).

### Evidencia 4: Consumo de Productos vía Gateway
- Navegador o Postman:
  - **GET** `http://localhost:8080/api/productos`
- **Captura:** Respuesta JSON (código HTTP 200).

### Evidencia 5 (Actividad): Consumo de Clientes vía Gateway
- Navegador o Postman:
  - **GET** `http://localhost:8080/api/clientes`
- **Captura:** Respuesta JSON (código HTTP 200).

### Evidencia 6 (Actividad): Consumo de Pedidos vía Gateway
- Navegador o Postman:
  - **GET** `http://localhost:8080/api/pedidos`
- **Captura:** Respuesta JSON (código HTTP 200).

---

## 📝 Conclusiones para el Informe

1. **Centralización y Punto Único de Entrada:** El uso de Spring Cloud Gateway permite abstraer la topología interna y la cantidad de microservicios hacia los clientes externos, exponiendo únicamente el puerto `8080` y protegiendo los puertos individuales de cada servicio.
2. **Descubrimiento Dinámico sin Hardcoding:** Al combinar Spring Cloud Gateway con Eureka (`uri: lb://NOMBRE-SERVICIO`), el enrutamiento se resuelve dinámicamente mediante el registro de nombres, eliminando direcciones IP o puertos fijos en el enrutador.
3. **Balanceo de Carga Integrado:** Con el protocolo `lb://` impulsado por *Spring Cloud LoadBalancer*, el Gateway distribuye automáticamente las peticiones entrantes entre múltiples instancias activas registradas en Eureka de forma transparente.
4. **Simplificación del Consumo Frontend/Cliente:** Los clientes (aplicaciones web, móviles o herramientas como Postman) solo interactúan con un único host y puerto base, reduciendo drásticamente problemas de CORS, configuraciones de red complejas y gestión de múltiples endpoints.
5. **Escalabilidad y Mantenibilidad:** La arquitectura permite agregar, retirar o escalar horizontalmente microservicios (como `cliente-service` y `pedido-service`) sin alterar la forma en que los consumidores externos realizan sus peticiones.
