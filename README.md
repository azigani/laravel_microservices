# GESCO Enterprise ERP - Microservices Ecosystem

Bienvenue dans le projet **GESCO**, un système ERP de classe entreprise ("Système Wahou") conçu avec une architecture microservices moderne, scalable et hautement disponible.

## 🚀 Vue d'ensemble de l'Architecture

Le système est composé de plusieurs microservices polyglottes (Spring Boot, NestJS, Laravel, FastAPI) communiquant via des protocoles synchrones (Feign/REST) et asynchrones (RabbitMQ).

### Services Coeur
1.  **Identity Service (Laravel)** : Gestion des utilisateurs, authentification JWT, et rôles.
2.  **Sales Service (Spring Boot)** : Logique métier des ventes, gestion des commandes.
3.  **Inventory Service (NestJS)** : Gestion des stocks, produits et catégories.
4.  **Document GED (Spring Boot)** : Gestion Electronique de Documents avec stockage objet (MinIO).
5.  **Analytics Service (FastAPI)** : Analyse des données de vente en temps réel (MongoDB).
6.  **Notification Service (Spring Boot)** : Envoi d'emails et alertes système.

### Infrastructure
-   **Discovery Server (Eureka)** : Annuaire dynamique des services.
-   **Config Server** : Centralisation des configurations.
-   **API Gateway (NestJS)** : Point d'entrée unique, routage et sécurité.
-   **Zipkin** : Tracing distribué pour la visibilité de bout-en-bout.

---

## 🛠️ Guide d'Installation (Étape par Étape)

### 1. Pré-requis
-   **Docker & Docker Compose** (RECOMMANDÉ)
-   Java 21+, Node.js 20+, PHP 8.2+, Python 3.11+ (si vous lancez hors Docker)
-   Client Git

### 2. Clonage et Configuration
```bash
git clone https://github.com/azigani/laravel_microservices.git
cd laramel_microservices
```

### 3. Lancement avec CLI "Expert" (Recommandé)
Pour une expérience professionnelle, utilisez le script de gestion fourni :

**Windows (cmd/PowerShell) :**
```bash
./gesco.bat up
```

**Linux / Mac / Git Bash :**
```bash
chmod +x gesco.sh
./gesco.sh up
```

#### Commandes CLI disponibles :
- `up` : Démarre tous les services en arrière-plan.
- `down` : Arrête et supprime les conteneurs.
- `build` : Reconstruit les images Docker.
- `logs` : Affiche les logs en temps réel.
- `status` : Affiche l'état de santé de chaque service.
- `doctor` (sh uniquement) : Vérifie vos pré-requis système.

---

### 4. Accès aux Tableaux de Bord
-   **Eureka (Discovery)** : [http://localhost:8761](http://localhost:8761) - *Vérifiez que tous les services sont UP.*
-   **Zipkin (Tracing)** : [http://localhost:9411](http://localhost:9411) - *Suivez vos requêtes.*
-   **RabbitMQ Management** : [http://localhost:15672](http://localhost:15672) (guest/guest)
-   **MinIO Console** : [http://localhost:9001](http://localhost:9001) (minioadmin/minioadmin)

---

## 🔗 Flux de Communication (Démonstration)

### Test Synchrone (Feign + Eureka)
Appelez le point de terminaison de test dans Sales via la Gateway :
`GET http://localhost:3000/sales/api/system/status`
> **Effet** : Sales demande dynamiquement à Eureka l'adresse de `Inventory` et `Document`, les interroge, et vous renvoie un rapport complet.

### Test Asynchrone (RabbitMQ)
1. Créez une vente via `POST http://localhost:3000/sales/api/sales`.
2. Vérifiez les logs de `service-notification` et `service-analytics`.
> **Effet** : La vente déclenche un événement `sale.created`. Notification prépare un email et Analytics enregistre la métrique dans MongoDB.

---

## 🛡️ Sécurité & Tracing
Toutes les requêtes passent par la **Gateway** qui vérifie le JWT (via Identity Service) avant de router la requête. Chaque saut entre services injecte des headers de tracing injectés par **Micrometer/Brave**, visibles instantanément dans Zipkin.

---

## 📈 Développement & Workflow Git
-   **Branche principale** : `dev` (Intégration continue).
-   **Features** : Toujours créer une branche `feature/nom-de-la-feature` et fusionner vers `dev` après validation.
