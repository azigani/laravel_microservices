import { Order } from './order.entity';

export interface IOrderRepository {
    save(order: Order): Promise<Order>;
    findById(id: string): Promise<Order | null>;
    update(order: Order): Promise<Order>;
}

export const IOrderRepository = Symbol('IOrderRepository');
