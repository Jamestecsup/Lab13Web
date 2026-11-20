@echo off
title Detener Microservicios y Gateway
echo Deteniendo procesos en puertos 8761, 8080, 8082, 8083, 8084, 8085...

for %%P in (8761 8080 8082 8083 8084 8085) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%P ^| findstr LISTENING') do (
        echo Terminando proceso en puerto %%P (PID %%a)...
        taskkill /F /PID %%a >nul 2>&1
    )
)

echo Microservicios y Gateway detenidos correctamente.
pause
