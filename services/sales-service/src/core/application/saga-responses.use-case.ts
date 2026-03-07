import { Injectable, Inject } from '@nestjs/common';
import { IOrderRepository } from '../domain/order.repository.interface';
import { OrderStatus } from '../domain/order.entity';

@Injectable()
export class ProcessStockReservedUseCase {
    constructor(
        @Inject(IOrderRepository)
        private readonly orderRepository: IOrderRepository,
    ) { }

    async execute(orderId: string): Promise<void> {
        const order = await this.orderRepository.findById(orderId);
        if (order) {
            order.markAsReserved();
            // On pourrait imaginer ici une transition vers l'état COMPLETED si le paiement est déjà fait
            order.markAsCompleted();
            await this.orderRepository.update(order);
            console.log(`[Sales Saga] Order ${orderId} successfully COMPLETED after stock reservation.`);
        }
    }
}

@Injectable()
export class ProcessStockUnavailableUseCase {
    constructor(
        @Inject(IOrderRepository)
        private readonly orderRepository: IOrderRepository,
    ) { }

    async execute(orderId: string, reason: string): Promise<void> {
        const order = await this.orderRepository.findById(orderId);
        if (order) {
            order.cancel();
            await this.orderRepository.update(order);
            console.warn(`[Sales Saga] Order ${orderId} CANCELED. Reason: ${reason}`);
        }
    }
}
