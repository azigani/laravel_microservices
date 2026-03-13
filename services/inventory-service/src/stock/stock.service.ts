import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Stock } from './stock.entity';
import { UpdateStockDto, AdjustStockDto } from './stock.dto';
import { ProductService } from '../product/product.service';

@Injectable()
export class StockService {
    constructor(
        @InjectRepository(Stock)
        private readonly stockRepo: Repository<Stock>,
        private readonly productService: ProductService,
    ) { }

    async findByProductId(productId: number): Promise<Stock> {
        let stock = await this.stockRepo.findOne({ where: { productId } });

        // Si pas de stock encore, on le crée à la volée (Lazy creation)
        if (!stock) {
            await this.productService.findOne(productId); // Vérifie que le produit existe
            stock = this.stockRepo.create({ productId, quantity: 0 });
            await this.stockRepo.save(stock);
        }

        return stock;
    }

    async update(productId: number, dto: UpdateStockDto): Promise<Stock> {
        const stock = await this.findByProductId(productId);
        Object.assign(stock, dto);
        return this.stockRepo.save(stock);
    }

    async adjust(productId: number, dto: AdjustStockDto): Promise<Stock> {
        const stock = await this.findByProductId(productId);
        stock.quantity += dto.amount;

        if (stock.quantity < 0) {
            throw new Error('Le stock ne peut pas devenir négatif');
        }

        return this.stockRepo.save(stock);
    }

    async checkAndEmitAlert(productId: number) {
        const stock = await this.findByProductId(productId);
        const ALERT_THRESHOLD = 5;

        if (stock.quantity <= ALERT_THRESHOLD) {
            console.warn(`LOW STOCK ALERT: Product ${productId} is at ${stock.quantity}`);
            // In a real wahou project, we would emit to another exchange here
            // this.client.emit('stock.low', { productId, quantity: stock.quantity });
        }
    }
}
