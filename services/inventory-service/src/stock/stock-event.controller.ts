import { Controller } from '@nestjs/common';
import { MessagePattern, Payload } from '@nestjs/microservices';
import { StockService } from './stock.service';

@Controller()
export class StockEventController {
    constructor(private readonly stockService: StockService) { }

    @MessagePattern('order.placed')
    async handleOrderPlaced(@Payload() data: any) {
        console.log(`[Inventory] Received order.placed: ${data.orderId}`, data);
        return await this.stockService.reserveStock(data);
    }
}
