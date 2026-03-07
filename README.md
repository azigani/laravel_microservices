# GESCO - Gestion Commerciale Microservices

Ce projet est une architecture microservices polyglotte et robuste pour un système ERP GESCO.

## 🚀 Écosystème Polyglotte
- **Identity Service** : Laravel (PHP) - Authentification & JWT.
- **Gateway** : NestJS (TypeScript) - Proxy & Sécurité.
- **Inventory Service** : NestJS (TypeScript) - Gestion des stocks (DDD).
- **Sales Service** : NestJS (TypeScript) - Gestion des commandes & Saga Pattern (DDD).
- **Analytics Service** : Python (FastAPI) - Statistiques temps-réel & MongoDB.
- **Notification Service** : Go (Gin) - [À VENIR] Mails & Temps réel.
- **Document Service** : Python (FastAPI) - [À VENIR] Génération de PDF & Rapports.

## 🛠️ Installation & Démarrage
1. **Prérequis** : Docker & Docker Compose.
2. **Lancement** :
   ```bash
   docker-compose up -d --build
   ```
3. **Services** :
   - Gateway : `http://localhost:3000`
   - Sales : `http://localhost:3002`
   - Analytics : `http://localhost:8001`

## 📊 Flux Événementiel (RabbitMQ)
Les microservices communiquent de manière asynchrone via des événements (`order.placed`, `stock.reserved`, etc.) pour garantir la haute disponibilité et la cohérence éventuelle.

## 🏗️ Architecture
Chaque service suit les principes de la **Clean Architecture** et du **DDD** (Domain-Driven Design) pour assurer la testabilité et la maintenance.
