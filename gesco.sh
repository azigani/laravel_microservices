#!/bin/bash

# GESCO Management Script - Expert Mode
# Usage: ./gesco.sh [command]

COLOR_BLUE='\033[0;34m'
COLOR_GREEN='\033[0;32m'
COLOR_RED='\033[0;31m'
COLOR_YELLOW='\033[1;33m'
NC='\033[0m' # No Color

function print_blue() { echo -e "${COLOR_BLUE}$1${NC}"; }
function print_green() { echo -e "${COLOR_GREEN}$1${NC}"; }
function print_red() { echo -e "${COLOR_RED}$1${NC}"; }
function print_yellow() { echo -e "${COLOR_YELLOW}$1${NC}"; }

function show_help() {
    echo "GESCO Microservices Management Tool"
    echo ""
    echo "Usage: ./gesco.sh [command]"
    echo ""
    echo "Commands:"
    echo "  up        Start all microservices in background"
    echo "  down      Stop and remove containers"
    echo "  restart   Restart all services"
    echo "  build     Rebuild all service images"
    echo "  logs      Tail logs for all services (or ./gesco.sh logs <service_name>)"
    echo "  status    Show status of all containers"
    echo "  clean     Stop and remove all containers, images, and volumes"
    echo "  doctor    Check system requirements"
}

function check_doctor() {
    print_blue "🔍 Checking system requirements..."
    
    if command -v docker >/dev/null 2>&1; then
        print_green "✅ Docker is installed"
    else
        print_red "❌ Docker is not installed"
    fi

    if command -v docker-compose >/dev/null 2>&1; then
        print_green "✅ Docker Compose is installed"
    else
        print_red "❌ Docker Compose is not installed"
    fi
}

case "$1" in
    up)
        print_blue "🚀 Starting GESCO Ecosystem..."
        docker-compose up -d
        print_green "✨ Services are starting. Use './gesco.sh status' to monitor."
        ;;
    down)
        print_yellow "🛑 Stopping GESCO Ecosystem..."
        docker-compose down
        print_green "✅ System stopped."
        ;;
    restart)
        print_blue "🔄 Restarting services..."
        docker-compose restart
        ;;
    build)
        print_blue "🏗️ Building service images..."
        docker-compose build
        ;;
    logs)
        if [ -z "$2" ]; then
            docker-compose logs -f
        else
            docker-compose logs -f "$2"
        fi
        ;;
    status)
        print_blue "📊 Current Service Status:"
        docker-compose ps
        ;;
    clean)
        print_red "WARN: This will remove everything (volumes included)!"
        read -p "Are you sure? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            docker-compose down -v --rmi all
            print_green "✅ System cleaned."
        fi
        ;;
    doctor)
        check_doctor
        ;;
    *)
        show_help
        ;;
esac
