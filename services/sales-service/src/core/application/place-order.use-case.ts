import { Inject, Injectable } from '@nestjs/common';
import { IOrderRepository } from '../domain/order.repository.interface';
import { Order, OrderItem, OrderStatus } from '../domain/order.entity';
import { IMessageBroker } from '../domain/message-broker.interface';
import { OrderPlacedEvent } from '../domain/events/order-placed.event';

export class PlaceOrderDto {
    customerId: string;
    items: {
        productId: string;
        quantity: number;
        unitPrice: number;
    }[];
}

@Injectable()
export class PlaceOrderUseCase {
    constructor(
        @Inject(IOrderRepository)
        private readonly orderRepository: IOrderRepository,
        @Inject(IMessageBroker)
        private readonly messageBroker: IMessageBroker,
    ) { }

    async execute(dto: PlaceOrderDto): Promise<Order> {
        const items = dto.items.map(
            (item) => new OrderItem(item.productId, item.quantity, item.unitPrice),
        );

        const orderId = Math.random().toString(36).substring(7);
        const order = new Order(
            orderId,
            dto.customerId,
            items,
            OrderStatus.PENDING,
        );

        // Persistence initiale (État: PENDING)
        const savedOrder = await this.orderRepository.save(order);

        // Émission de l'événement pour la Saga (Vérification de stock)
        await this.messageBroker.publish(new OrderPlacedEvent(
            savedOrder.id,
            savedOrder.customerId,
            savedOrder.items.map(i => ({ productId: i.productId, quantity: i.quantity })),
            savedOrder.totalAmount
        ));

        return savedOrder;
    }
}
