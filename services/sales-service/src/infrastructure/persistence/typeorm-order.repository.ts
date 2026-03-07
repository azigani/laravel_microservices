import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { IOrderRepository } from '../../core/domain/order.repository.interface';
import { Order, OrderItem } from '../../core/domain/order.entity';
import { OrderTypeOrm, OrderItemTypeOrm } from './order.orm-entity';

@Injectable()
export class TypeOrmOrderRepository implements IOrderRepository {
    constructor(
        @InjectRepository(OrderTypeOrm)
        private readonly orderRepo: Repository<OrderTypeOrm>,
        @InjectRepository(OrderItemTypeOrm)
        private readonly itemRepo: Repository<OrderItemTypeOrm>,
    ) { }

    async save(order: Order): Promise<Order> {
        const ormOrder = new OrderTypeOrm();
        ormOrder.id = order.id;
        ormOrder.customerId = order.customerId;
        ormOrder.status = order.status;
        ormOrder.totalAmount = order.totalAmount;

        await this.orderRepo.save(ormOrder);

        const ormItems = order.items.map((item) => {
            const ormItem = new OrderItemTypeOrm();
            ormItem.id = Math.random().toString(36).substring(7);
            ormItem.orderId = order.id;
            ormItem.productId = item.productId;
            ormItem.quantity = item.quantity;
            ormItem.unitPrice = item.unitPrice;
            return ormItem;
        });

        await this.itemRepo.save(ormItems);

        return order;
    }

    async findById(id: string): Promise<Order | null> {
        // Logic to fetch and map back to Domain Entity
        const ormOrder = await this.orderRepo.findOne({ where: { id } });
        if (!ormOrder) return null;

        const ormItems = await this.itemRepo.find({ where: { orderId: id } });
        const domainItems = ormItems.map(
            (item) => new OrderItem(item.productId, item.quantity, Number(item.unitPrice)),
        );

        return new Order(ormOrder.id, ormOrder.customerId, domainItems, ormOrder.status, ormOrder.createdAt);
    }

    async update(order: Order): Promise<Order> {
        await this.orderRepo.update(order.id, { status: order.status });
        return order;
    }
}
