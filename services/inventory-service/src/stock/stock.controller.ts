import { Controller, Get, Post, Body, Param, ParseIntPipe, Patch } from '@nestjs/common';
import { StockService } from './stock.service';
import { UpdateStockDto, AdjustStockDto } from './stock.dto';

@Controller('stock')
export class StockController {
    constructor(private readonly stockService: StockService) { }

    @Get(':productId')
    findByProductId(@Param('productId', ParseIntPipe) productId: number) {
        return this.stockService.findByProductId(productId);
    }

    @Patch(':productId')
    update(
        @Param('productId', ParseIntPipe) productId: number,
        @Body() updateStockDto: UpdateStockDto,
    ) {
        return this.stockService.update(productId, updateStockDto);
    }

    @Post(':productId/adjust')
    adjust(
        @Param('productId', ParseIntPipe) productId: number,
        @Body() adjustStockDto: AdjustStockDto,
    ) {
        return this.stockService.adjust(productId, adjustStockDto);
    }
}
