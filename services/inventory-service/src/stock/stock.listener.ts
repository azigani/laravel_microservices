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
            // Restore stock
            await this.stockService.adjust(productId, { amount: quantity });
        } else {
            // New sale (reduce stock)
            await this.stockService.adjust(productId, { amount: -quantity });
        }

        // Proactive Stock Alert Check
        await this.stockService.checkAndEmitAlert(productId);
    }
}
