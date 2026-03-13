@echo off
REM GESCO Management Script - Windows Mode
REM Usage: gesco [command]

if "%1" == "up" (
    echo [GESCO] Starting services...
    docker-compose up -d
    goto end
)

if "%1" == "down" (
    echo [GESCO] Stopping services...
    docker-compose down
    goto end
)

if "%1" == "restart" (
    echo [GESCO] Restarting services...
    docker-compose restart
    goto end
)

if "%1" == "build" (
    echo [GESCO] Building images...
    docker-compose build
    goto end
)

if "%1" == "logs" (
    if "%2" == "" (
        docker-compose logs -f
    ) else (
        docker-compose logs -f %2
    )
    goto end
)

if "%1" == "status" (
    echo [GESCO] Current Status:
    docker-compose ps
    goto end
)

if "%1" == "" (
    echo GESCO Management Tool
    echo.
    echo Usage: gesco [command]
    echo.
    echo Commands:
    echo   up        Start services
    echo   down      Stop services
    echo   restart   Restart services
    echo   build     Build images
    echo   logs      Show logs
    echo   status    Show status
)

:end
