@echo off
title Iniciar Todos los Microservicios con API Gateway
echo ========================================================
echo Iniciando Arquitectura de Microservicios Tecsup - Lab 13
echo Service Discovery (Eureka) + API Gateway (Spring Cloud)
echo ========================================================

echo 1. Iniciando Eureka Server (Puerto 8761)...
start "Eureka Server - 8761" cmd /k "title Eureka Server (8761) && java -jar eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar"

echo Esperando 12 segundos a que Eureka Server levante...
timeout /t 12 /nobreak >nul

echo 2. Iniciando Categoria Service (Puerto 8082)...
start "Categoria Service - 8082" cmd /k "title Categoria Service (8082) && java -jar categoria-service\target\categoria-service-0.0.1-SNAPSHOT.jar"

echo 3. Iniciando Producto Service (Puerto 8083)...
start "Producto Service - 8083" cmd /k "title Producto Service (8083) && java -jar producto-service\target\producto-service-0.0.1-SNAPSHOT.jar"

echo 4. Iniciando Cliente Service (Puerto 8084)...
start "Cliente Service - 8084" cmd /k "title Cliente Service (8084) && java -jar cliente-service\target\cliente-service-0.0.1-SNAPSHOT.jar"

echo 5. Iniciando Pedido Service (Puerto 8085)...
start "Pedido Service - 8085" cmd /k "title Pedido Service (8085) && java -jar pedido-service\target\pedido-service-0.0.1-SNAPSHOT.jar"

echo Esperando 8 segundos antes de iniciar Gateway Service...
timeout /t 8 /nobreak >nul

echo 6. Iniciando Gateway Service (Puerto 8080)...
start "Gateway Service - 8080" cmd /k "title Gateway Service (8080) && java -jar gateway-service\target\gateway-service-0.0.1-SNAPSHOT.jar"

echo ========================================================
echo Todos los microservicios y el API Gateway han sido lanzados.
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway Base: http://localhost:8080
echo ========================================================
pause
