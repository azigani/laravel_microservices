# GESCO - Gestion Commerciale Microservices

Ce projet est une architecture microservices polyglotte et robuste pour un système ERP GESCO.

## 🚀 Écosystème Polyglotte
- **Identity Service** : [Laravel (PHP)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/identity-service) - Authentification & JWT.
- **Gateway** : [NestJS (TypeScript)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/gateway) - Proxy & Sécurité.
- **Inventory Service** : [NestJS (TypeScript)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/inventory-service) - Gestion des stocks (DDD).
- **Sales Service** : [Spring Boot (Java)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/service-sales) - Gestion des commandes (DDD).
- **Notification Service** : [Spring Boot (Java)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/service-notification) - Mails & Temps réel (DDD).
- **Analytics Service** : [Django REST Framework (Python)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/service-analytics) - Statistiques temps-réel & MongoDB.
- **Document Service** : [Laravel (PHP)](file:///c:/Users/lenovo/Documents/Laramel_microservices/services/service-document) - Génération de PDF & Rapports.

## 📊 Flux Événementiel (RabbitMQ)
Les microservices communiquent de manière asynchrone via des événements (`order.placed`, `stock.reserved`, etc.) pour garantir la haute disponibilité et la cohérence éventuelle.

## 🏗️ Architecture
Chaque service suit les principes de la **Clean Architecture** et du **DDD** (Domain-Driven Design) pour assurer la testabilité et la maintenance.

## 🛠️ Installation & Démarrage (Docker)
1. **Prérequis** : Docker & Docker Compose.
2. **Cloner le projet**.
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
- **Voir les logs** : `docker-compose logs -f <service_name>`

### Base de données & Migrations
- **Identité (Laravel)** :
  ```bash
  docker-compose exec identity-service php artisan migrate
  ```
- **Inventory (NestJS/TypeORM)** :
  *(Les migrations sont généralement automatiques via `synchronize: true` en développement)*

## 🌐 Accès aux Services
- **Gateway API** : `http://localhost:3000`
- **Identity API (via Gateway)** : `http://localhost:3000/auth/*`
- **Inventory API (via Gateway)** : `http://localhost:3000/inventory/*`
- **MailDev / MailHog** : `http://localhost:10080` (Interface de capture d'emails)

---
*Documentation générée pour faciliter l'onboarding des nouveaux développeurs.*
