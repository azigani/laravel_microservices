# 🚀 GESCO ERP - Microservices Architecture

> **Gestion Commerciale Complète** — Enterprise-grade ERP system built with a polyglot microservices architecture.

---

## 📐 Architecture Overview

```
                    ┌─────────────────┐
                    │   API Gateway   │ (NestJS - port 3000)
                    │   JWT + Proxy   │
                    └───────┬─────────┘
                            │
         ┌──────────────────┼──────────────────────┐
         │                  │                      │
  ┌──────▼──────┐   ┌──────▼──────┐   ┌───────────▼───────────┐
  │  Identity   │   │  Inventory  │   │   Spring Boot Cloud   │
  │  (Laravel)  │   │  (NestJS)   │   │  Sales │ Notif │ GED  │
  │  port 8000  │   │  port 3001  │   │  8083  │ 8084  │ 8085 │
  └──────┬──────┘   └──────┬──────┘   └───────────┬───────────┘
         │                  │                      │
         └──────────────────┼──────────────────────┘
                            │
         ┌──────────────────┼──────────────────────┐
         │                  │                      │
  ┌──────▼──────┐   ┌──────▼──────┐   ┌───────────▼──────┐
  │  PostgreSQL │   │  RabbitMQ   │   │  MinIO (S3)      │
  │  port 5432  │   │  port 5672  │   │  port 9000/9001  │
  └─────────────┘   └─────────────┘   └──────────────────┘
```

### Infrastructure Services
| Service | Technology | Port | Role |
|---------|-----------|------|------|
| **Discovery Server** | Spring Cloud Eureka | 8761 | Service registry & discovery |
| **Config Server** | Spring Cloud Config | 8888 | Centralized configuration (native profile) |

### Business Services
| Service | Technology | Port | Role |
|---------|-----------|------|------|
| **Identity Service** | Laravel (PHP) | 8000 | Auth, users, roles, JWT |
| **Gateway** | NestJS | 3000 | API proxy, JWT validation, routing |
| **Inventory Service** | NestJS + TypeORM | 3001 | Products, categories, stock |
| **Sales Service** | Spring Boot + JPA | 8083 | Sales, invoicing, events |
| **Notification Service** | Spring Boot + RabbitMQ | 8084 | Email, SMS, push notifications |
| **Document Service (GED)** | Spring Boot + JasperReports | 8085 | Folders, documents, versioning, PDF |

### Infrastructure
| Service | Technology | Port | Role |
|---------|-----------|------|------|
| **PostgreSQL** | PostgreSQL 15 | 5432 | Relational database |
| **MongoDB** | MongoDB | 27017 | Document storage (analytics) |
| **RabbitMQ** | RabbitMQ | 5672/15672 | Async messaging |
| **Redis** | Redis | 6379 | Caching & sessions |
| **MinIO** | MinIO (S3) | 9000/9001 | File storage (GED) |

---

## 🏗️ Architecture Pattern: Clean Architecture + DDD

All business services follow the same layered structure:

```
service-*/
├── core/
│   ├── domain/
│   │   ├── model/       # Entities (pure business logic, no framework)
│   │   └── port/        # Interfaces (repositories, storage, events)
│   └── application/
│       ├── dto/          # Request/Response DTOs
│       └── usecase/      # Business use cases
└── infrastructure/
    ├── adapters/
    │   ├── persistence/  # JPA entities, repositories, adapters
    │   ├── messaging/    # RabbitMQ publishers/listeners
    │   ├── storage/      # MinIO adapter
    │   └── rest/         # REST controllers
    └── config/           # Spring beans, RabbitMQ, MinIO configs
```

---

## 🔄 Inter-Service Communication

### Async (Event-Driven via RabbitMQ)
- `sale.created` → **Notification Service** (sends confirmation)
- `sale.created` → **Document Service** (generates PDF invoice via JasperReports)

### Sync (REST via Gateway)
- All external API calls go through the **Gateway** (`http://localhost:3000`)
- Gateway proxies to internal services and validates JWT

---

## 🚀 Quick Start

```bash
# 1. Clone the project
git clone https://github.com/azigani/laravel_microservices.git
cd laravel_microservices

# 2. Start all services
docker-compose up -d

# 3. Access services
# Gateway:        http://localhost:3000
# Eureka:         http://localhost:8761
# Config Server:  http://localhost:8888
# RabbitMQ UI:    http://localhost:15672 (guest/guest)
# MinIO Console:  http://localhost:9001 (minioadmin/minioadmin)
```

---

## 🌿 Git Workflow (Team Standards)

```
main ─── dev ─── feature/xxx
              └── feature/yyy
```

1. **Branch** from `dev`: `git checkout -b feature/my-feature`
2. **Commit** with Conventional Commits: `feat(sales): add invoice generation`
3. **Push** feature branch: `git push -u origin feature/my-feature`
4. **Merge** into `dev`: `git checkout dev && git merge feature/my-feature`
5. **Push** `dev`: `git push origin dev`

### Commit Convention
| Prefix | Usage |
|--------|-------|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Code restructure |
| `docs` | Documentation |
| `chore` | Build/config changes |

---

## 📁 Databases

| Database | Service | Engine |
|----------|---------|--------|
| `gesco_identity` | Identity Service | PostgreSQL |
| `gesco_inventory` | Inventory Service | PostgreSQL |
| `gesco_sales` | Sales Service | PostgreSQL |
| `gesco_document` | Document Service (GED) | PostgreSQL |
| `gesco_notification` | Notification Service | PostgreSQL |

All databases are auto-created by `docker/postgres/init.sql`.

---

## 📝 License

Proprietary — GESCO ERP by Laramel.
