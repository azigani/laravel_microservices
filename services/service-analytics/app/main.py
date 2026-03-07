import asyncio
import json
import os
from fastapi import FastAPI
from motor.motor_asyncio import AsyncIOMotorClient
import aio_pika
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(title="GESCO Analytics Service", version="1.0.0")

# Configuration
MONGODB_URL = os.getenv("MONGODB_URL", "mongodb://mongodb:27017")
RABBITMQ_URL = os.getenv("RABBITMQ_URL", "amqp://guest:guest@rabbitmq:5672")

# Database client
client = AsyncIOMotorClient(MONGODB_URL)
db = client.gesco_analytics

@app.get("/health")
async def health():
    return {"status": "healthy", "service": "analytics"}

@app.get("/stats")
async def get_stats():
    orders_count = await db.sales_metrics.count_documents({})
    # Basic aggregation example
    pipeline = [
        {"$group": {"_id": None, "total_revenue": {"$sum": "$totalAmount"}}}
    ]
    cursor = db.sales_metrics.aggregate(pipeline)
    revenue_list = await cursor.to_list(length=1)
    
    return {
        "total_orders": orders_count,
        "total_revenue": revenue_list[0]["total_revenue"] if revenue_list else 0
    }

async def process_message(message: aio_pika.IncomingMessage):
    async with message.process():
        event = json.loads(message.body.decode())
        event_name = message.routing_key
        print(f" [Analytics] Processing {event_name}: {event}")
        
        # Store event in MongoDB for long-term analytics
        await db.events_log.insert_one({
            "event_name": event_name,
            "payload": event,
            "processed_at": asyncio.get_event_loop().time()
        })
        
        if event_name == "order.placed":
            await db.sales_metrics.insert_one(event)
            print(f" [Analytics] Metric recorded for order {event.get('orderId')}")

async def consume_rabbitmq():
    connection = await aio_pika.connect_robust(RABBITMQ_URL)
    channel = await connection.channel()

    # Déclaration de la queue
    queue = await channel.declare_queue('analytics_queue', durable=False)
    
    # Liaison aux événements via l'échange par défaut ou un échange spécifique
    # Ici on suppose que le sales-service émet sur l'exchange amq.topic par exemple
    # ou directement via routing key si exchange par défaut
    await queue.bind(exchange='amq.topic', routing_key='order.placed')
    await queue.bind(exchange='amq.topic', routing_key='stock.reserved')

    print(' [Analytics] Waiting for events. To exit press CTRL+C')
    await queue.consume(process_message)

@app.on_event("startup")
async def startup_event():
    # Lancement du consommateur en tâche de fond
    asyncio.create_task(consume_rabbitmq())
