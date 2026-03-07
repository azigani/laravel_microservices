import pika
import json
import os
import threading
from .mongodb import get_stats_collection

def process_sale_event(ch, method, properties, body):
    data = json.loads(body)
    print(f" [x] Processing sale: {data.get('orderId', 'unknown')}")
    
    collection = get_stats_collection()
    
    # Update total sales and order count
    collection.update_one(
        {"_id": "global_stats"},
        {
            "$inc": {
                "total_sales": float(data.get('totalAmount', 0)),
                "order_count": 1
            }
        },
        upsert=True
    )
    ch.basic_ack(delivery_tag=method.delivery_tag)

def start_consumer():
    rabbitmq_url = os.getenv('RABBITMQ_URL', 'amqp://guest:guest@rabbitmq/')
    params = pika.URLParameters(rabbitmq_url)
    connection = pika.BlockingConnection(params)
    channel = connection.channel()

    channel.queue_declare(queue='analytics_queue', durable=True)
    channel.queue_bind(exchange='sales.exchange', queue='analytics_queue', routing_key='sale.created')

    channel.basic_consume(queue='analytics_queue', on_message_callback=process_sale_event)

    print(' [*] Waiting for sale events. To exit press CTRL+C')
    channel.start_consuming()

def run_consumer_async():
    thread = threading.Thread(target=start_consumer, daemon=True)
    thread.start()
