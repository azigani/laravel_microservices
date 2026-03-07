import { Controller, Post, Body, Get, Param } from '@nestjs/common';
import { PlaceOrderUseCase, PlaceOrderDto } from '../../core/application/place-order.use-case';

@Controller('orders')
export class OrderController {
    constructor(private readonly placeOrderUseCase: PlaceOrderUseCase) { }

    @Post()
    async placeOrder(@Body() dto: PlaceOrderDto) {
        return await this.placeOrderUseCase.execute(dto);
    }

    // Potential for Get methods here
}
