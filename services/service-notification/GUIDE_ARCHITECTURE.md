# Guide Architectural : Service de Notification (Spring Boot)

Ce document explique les choix architecturaux fait pour ce microservice afin d'aider l'équipe à maintenir et faire évoluer le code.

## 🏗️ Architecture Hexagonale (Clean Architecture)

Nous avons choisi l'architecture hexagonale pour isoler le **cœur métier** des détails techniques.

### Structure des dossiers
- `core.domain` : Contient les règles métier pures (Entités, Value Objects, Interfaces de ports). **Zéro dépendance au framework ici.**
- `core.application` : Contient les Cas d'Utilisation (Use Cases) qui orchestrent le métier.
- `infrastructure.adapters` : Contient le code technique (RabbitMQ, JavaMail, Persistence). Ce sont les "adaptateurs" qui se branchent sur les "ports" du core.

## 💎 Principes Clés

### 1. DDD (Domain-Driven Design)
- **Entities** : Objets avec une identité unique (ex: `Notification`).
- **Value Objects** : Objets définis par leurs attributs (ex: `EmailAddress`). Ils sont immuables et s'auto-valident à la création.

### 2. SOLID & Dépendances
- **DIP (Dependency Inversion)** : Le métier ne dépend jamais de l'infrastructure. C'est l'infrastructure qui implémente les interfaces définies par le métier.
- **Single Responsibility** : Chaque classe a un rôle unique (Mapper pour transformer, Port pour définir l'accès, Use Case pour l'action).

### 3. Mapping de Couche
Nous utilisons des **Mappers (MapStruct)** pour transformer les objets entre les couches (ex: de DTO RabbitMQ vers Entité Domaine). Cela évite que les détails de l'API externe ne polluent le cœur métier.

## ✅ Avantages & Inconvénients

| Avantages | Inconvénients |
| :--- | :--- |
| **Testabilité** : On peut tester le métier sans base de données ni serveur mail. | **Verbosité** : Plus de classes et de fichiers qu'un projet CRUD simple. |
| **Flexibilité** : Changer de broker (RabbitMQ -> Kafka) impacte 1 seul dossier. | **Complexité** : Demande une compréhension des patterns de Clean Architecture. |
| **Sécurité** : Les Value Objects garantissent des données toujours valides. | |

---
*Document généré par l'Expert Architecte pour l'équipe GESCO.*
