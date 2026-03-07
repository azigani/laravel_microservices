# Analytics Service (Python / FastAPI)

Ce service est responsable de la collecte et de l'analyse des données de vente en temps réel dans l'écosystème GESCO.

## Architecture
- **Framework** : FastAPI
- **Base de données** : MongoDB (via Motor pour l'asynchronisme)
- **Messaging** : RabbitMQ (Consomme les événements `order.placed` et `stock.reserved`)

## Installation locale (Team dev)
1. Créer un environnement virtuel : `python -m venv venv`
2. Activer : `source venv/bin/activate` (Linux/Mac) ou `venv\Scripts\activate` (Windows)
3. Installer : `pip install -r requirements.txt`
4. Lancer : `uvicorn app.main:app --reload --port 8001`

## Docker
Le service est orchestré via le `docker-compose.yml` à la racine.
Image : `gesco_analytics_service`
Port : `8001`
