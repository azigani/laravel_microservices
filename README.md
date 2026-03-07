<<<<<<< HEAD
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
=======
# GESCO Microservices Project

Ce projet est une architecture microservices polyglotte et robuste pour un système ERP GESCO.

## 🚀 Écosystème Polyglotte
- **Identity Service** : Laravel (PHP) - Authentification & JWT.
- **Gateway** : NestJS (TypeScript) - Proxy & Sécurité.
- **Inventory Service** : NestJS (TypeScript) - Gestion des stocks (DDD).
- **Sales Service** : NestJS (TypeScript) - Gestion des commandes & Saga Pattern (DDD).
- **Analytics Service** : Python (FastAPI) - Statistiques temps-réel & MongoDB.
- **Notification Service** : Spring Boot (Java) - Mails & Temps réel (DDD).
- **Document Service** : Python (FastAPI) - [À VENIR] Génération de PDF & Rapports.

## 📊 Flux Événementiel (RabbitMQ)
Les microservices communiquent de manière asynchrone via des événements (`order.placed`, `stock.reserved`, etc.) pour garantir la haute disponibilité et la cohérence éventuelle.

## 🏗️ Architecture
Chaque service suit les principes de la **Clean Architecture** et du **DDD** (Domain-Driven Design) pour assurer la testabilité et la maintenance.

## 🛠️ Installation & Démarrage (Docker)
1. **Prérequis** : Docker & Docker Compose.
2. **Cloner le projet**
3. **Configurer les environnements** :
   Assurez-vous que les fichiers `.env` existent dans chaque service (`services/gateway/.env`, `services/identity-service/.env`, etc.).
4. **Lancement** :
   ```bash
   docker-compose up -d --build
   ```

## 🛠️ Commandes Docker Essentielles

### Gestion des containers
- **Démarrer tous les services** : `docker-compose up -d`
- **Arrêter tous les services** : `docker-compose down`
- **Reconstruire un service spécifique** : `docker-compose up -d --build <service_name>`

### Base de données & Migrations
- **Identité (Laravel)** :
  ```bash
  # Créer la DB (si nécessaire la première fois)
  docker exec -it gesco_postgres psql -U admin -c "CREATE DATABASE gesco_identity;"
  
  # Lancer les migrations
  docker-compose exec identity-service php artisan migrate
  ```
- **Inventory (NestJS/TypeORM)** :
  *(Les migrations sont généralement automatiques via `synchronize: true` en développement)*
  ```bash
  # Créer la DB (si nécessaire)
  docker exec -it gesco_postgres psql -U admin -c "CREATE DATABASE gesco_inventory;"
  ```

### Troubleshooting
- **Conflit de port 5432 (Postgres)** :
  Si vous avez une erreur d'authentification ou de port déjà utilisé, vérifiez qu'aucun autre Postgres local ou container ne tourne :
  ```bash
  docker stop ms_pgsql # (Exemple de container conflictuel courant)
  docker-compose up -d postgres
  ```

## 🌐 Accès aux Services
- **Gateway API** : `http://localhost:3000`
- **Identity API (via Gateway)** : `http://localhost:3000/auth/*`
- **Inventory API (via Gateway)** : `http://localhost:3000/inventory/*`
- **MailDev / MailHog** : `http://localhost:10080` (Interface de capture d'emails)

---
*Documentation générée pour faciliter l'onboarding des nouveaux développeurs.*
=========
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
>>>>>>>>> Temporary merge branch 2
