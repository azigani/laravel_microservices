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
    payments_count = await db.payments_log.count_documents({"status": "SUCCESS"})
    
    # Revenue aggregation
    pipeline = [
        {"$group": {"_id": None, "total_revenue": {"$sum": "$totalAmount"}}}
    ]
    cursor = db.sales_metrics.aggregate(pipeline)
    sale_revenue_list = await cursor.to_list(length=1)
    
    payment_pipeline = [
        {"$match": {"status": "SUCCESS"}},
        {"$group": {"_id": None, "total_paid": {"$sum": "$amount"}}}
    ]
    payment_cursor = db.payments_log.aggregate(payment_pipeline)
    payment_revenue_list = await payment_cursor.to_list(length=1)
    
    return {
        "total_orders": orders_count,
        "total_payments": payments_count,
        "potential_revenue": sale_revenue_list[0]["total_revenue"] if sale_revenue_list else 0,
        "confirmed_revenue": payment_revenue_list[0]["total_paid"] if payment_revenue_list else 0
    }

async def process_message(message: aio_pika.IncomingMessage):
    async with message.process():
        event = json.loads(message.body.decode())
        routing_key = message.routing_key
        print(f" [Analytics] Processing event {routing_key}")
        
        # Store all events for audit/history
        await db.events_history.insert_one({
            "type": routing_key,
            "data": event,
            "received_at": asyncio.get_event_loop().time()
        })
        
        if routing_key == "sale.created":
            await db.sales_metrics.insert_one(event)
        elif routing_key == "payment.completed":
            await db.payments_log.insert_one(event)
            print(f" [Analytics] Confirmed payment recorded for Sale {event.get('saleId')}")

async def consume_rabbitmq():
    connection = await aio_pika.connect_robust(RABBITMQ_URL)
    channel = await connection.channel()

    # Sales Exchange
    sales_exchange = await channel.declare_exchange('sales.exchange', aio_pika.ExchangeType.TOPIC)
    
    # Payment Exchange
    payment_exchange = await channel.declare_exchange('payment.exchange', aio_pika.ExchangeType.TOPIC)
    
    queue = await channel.declare_queue('analytics_queue', durable=True)
    
    await queue.bind(exchange=sales_exchange, routing_key='sale.created')
    await queue.bind(exchange=payment_exchange, routing_key='payment.completed')

    print(' [Analytics] Waiting for events (sale.created, payment.completed)...')
    await queue.consume(process_message)

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(consume_rabbitmq())
