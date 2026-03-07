import { Controller } from '@nestjs/common';
import { MessagePattern, Payload } from '@nestjs/microservices';
import { ProcessStockReservedUseCase, ProcessStockUnavailableUseCase } from '../../core/application/saga-responses.use-case';

@Controller()
export class OrderSagaController {
    constructor(
        private readonly stockReservedUseCase: ProcessStockReservedUseCase,
        private readonly stockUnavailableUseCase: ProcessStockUnavailableUseCase,
    ) { }

    @MessagePattern('stock.reserved')
    async handleStockReserved(@Payload() data: { orderId: string }) {
        console.log(`[Sales Saga] Received stock.reserved for order ${data.orderId}`);
        return await this.stockReservedUseCase.execute(data.orderId);
    }

    @MessagePattern('stock.unavailable')
    async handleStockUnavailable(@Payload() data: { orderId: string; reason: string }) {
        console.log(`[Sales Saga] Received stock.unavailable for order ${data.orderId}`);
        return await this.stockUnavailableUseCase.execute(data.orderId, data.reason);
    }
}
