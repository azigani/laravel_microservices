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
git clone https://github.com/azigani/microservices_laravel_springboot_nestjs.git
cd microservices_laravel_springboot_nestjs
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

---

## 📈 DevOps & Observability

Pour assurer une qualité de code et une surveillance de niveau production, le projet inclut une stack DevOps complète.

### 1. Code Quality (SonarQube)
- **Lancement** : `./gesco.bat devops` ou `docker-compose -f docker/docker-compose.devops.yml up -d sonarqube`
- **Accès** : [http://localhost:9000](http://localhost:9000) (admin/admin)
- **Analyse** : Exécutez `mvn sonar:sonar` à la racine pour envoyer les rapports.

### 2. Monitoring (Prometheus & Grafana)
- **Prometheus** : [http://localhost:9090](http://localhost:9090) - Collecte les métriques de chaque microservice.
- **Grafana** : [http://localhost:3000](http://localhost:3000) (admin/admin) - Tableaux de bord pré-configurés pour Spring Boot.

### 3. Orchestration (Kubernetes)
Les manifestes se trouvent dans le dossier `k8s/`.
```bash
kubectl apply -f k8s/core/
kubectl apply -f k8s/business/
```

### 4. Automatisation (Ansible)
Déploiement automatisé :
```bash
cd ansible
ansible-playbook -i hosts.ini playbook-k8s-deploy.yml
```

---

## 🛡️ Git Workflow Professionnel

Nous suivons un workflow strict pour garantir la stabilité :

1.  **Main** : Code stable et prêt pour la production.
2.  **Develop** : Branche d'intégration pour les fonctionnalités terminées.
3.  **Feature/** : Toute nouvelle fonctionnalité doit avoir sa branche (ex: `feature/pricing-service`).
4.  **Commit Message Standard (Conventional Commits)** :
    - `feat(scope): ...` pour une nouvelle fonctionnalité.
    - `fix(scope): ...` pour une correction de bug.
    - `chore(devops): ...` pour les changements d'infrastructure.

---

## 📈 État des Communications Inter-services (Feign)

La communication via **Spring Cloud OpenFeign** est centralisée dans les packages `infrastructure/adapters/feign`.
- **Gestion des Erreurs** : Utilisation de `ErrorDecoder` personnalisé.
- **Résilience** : (En cours) Intégration de **Resilience4j** pour les Circuit Breakers.
