import { Controller } from '@nestjs/common';
import { EventPattern, Payload } from '@nestjs/microservices';
import { StockService } from './stock.service';

@Controller()
export class StockListener {
    constructor(private readonly stockService: StockService) { }

    @EventPattern('sale.created')
    async handleSaleCreated(@Payload() data: any) {
        console.log('StockListener: Received sale event', data);
        const { productId, quantity, status } = data;

        if (status === 'CANCELLED') {
            await this.stockService.adjust(productId, { amount: quantity });
        } else {
            await this.stockService.adjust(productId, { amount: -quantity });
        }
        await this.stockService.checkAndEmitAlert(productId);
    }

    @EventPattern('payment.completed')
    async handlePaymentCompleted(@Payload() data: any) {
        console.log('StockListener: Received payment confirmation', data);
        const { status, saleId } = data;

        if (status === 'SUCCESS') {
            log.info(`Stock officially committed for sale ${saleId}`);
            // Note: In a true "reservation" system, we would move from reserved to physical decrement
            // For now we just log the finalization as the adjustment was already done at sale creation.
        }
    }
}
