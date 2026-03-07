import { Injectable, NotFoundException, Inject } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Stock } from './stock.entity';
import { CreateStockDto, UpdateStockDto, AdjustStockDto } from './stock.dto';
import { ProductService } from '../product/product.service';
import { ClientProxy } from '@nestjs/microservices';

@Injectable()
export class StockService {
    constructor(
        @InjectRepository(Stock)
        private readonly stockRepository: Repository<Stock>,
        private readonly productService: ProductService,
        @Inject('SALES_SERVICE') private readonly client: ClientProxy,
    ) { }

    async reserveStock(data: any) {
        const { orderId, items } = data;
        console.log(`[Inventory] Processing reservation for order ${orderId}`);

        try {
            for (const item of items) {
                const stock = await this.stockRepository.findOne({ where: { productId: item.productId } });

                if (!stock || stock.quantity < item.quantity) {
                    console.error(`[Inventory] Stock insufficient for product ${item.productId}`);
                    this.client.emit('stock.unavailable', { orderId, reason: `Stock insufficient for ${item.productId}` });
                    return;
                }
            }

            // Si tous les articles sont disponibles, on déduit le stock
            for (const item of items) {
                const stock = await this.stockRepository.findOne({ where: { productId: item.productId } });
                if (stock) {
                    stock.quantity -= item.quantity;
                    await this.stockRepository.save(stock);
                }
            }

            console.log(`[Inventory] Stock reserved successfully for order ${orderId}`);
            this.client.emit('stock.reserved', { orderId });

        } catch (error) {
            console.error(`[Inventory] Error during reservation:`, error);
            this.client.emit('stock.unavailable', { orderId, reason: 'Internal error during reservation' });
        }
    }

    async findByProductId(productId: number): Promise<Stock> {
        let stock = await this.stockRepository.findOne({ where: { productId } });

        if (!stock) {
            await this.productService.findOne(productId);
            stock = this.stockRepository.create({ productId, quantity: 0 });
            await this.stockRepository.save(stock);
        }

        return stock;
    }

    async update(productId: number, dto: UpdateStockDto): Promise<Stock> {
        const stock = await this.findByProductId(productId);
        Object.assign(stock, dto);
        return this.stockRepository.save(stock);
    }

    async adjust(productId: number, dto: AdjustStockDto): Promise<Stock> {
        const stock = await this.findByProductId(productId);
        stock.quantity += dto.amount;

        if (stock.quantity < 0) {
            throw new Error('Le stock ne peut pas devenir négatif');
        }

        return this.stockRepository.save(stock);
    }
}
