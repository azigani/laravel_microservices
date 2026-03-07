import { Inject, Injectable } from '@nestjs/common';
import { ClientProxy, ClientProxyFactory, Transport } from '@nestjs/microservices';
import { IMessageBroker } from '../core/domain/message-broker.interface';
import { IDomainEvent } from '../core/domain/events/order-placed.event';

@Injectable()
export class RabbitMQMessageBroker implements IMessageBroker {
    private client: ClientProxy;

    constructor() {
        this.client = ClientProxyFactory.create({
            transport: Transport.RMQ,
            options: {
                urls: [process.env.RABBITMQ_URL || 'amqp://guest:guest@rabbitmq:5672'],
                queue: 'sales_queue',
                queueOptions: {
                    durable: false,
                },
            },
        });
    }

    async publish(event: IDomainEvent): Promise<void> {
        // On utilise l'eventName comme pattern de message
        this.client.emit(event.eventName, event);
        console.log(`[Messaging] Event published: ${event.eventName}`, event);
    }
}
