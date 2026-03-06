import {
    Injectable,
    NotFoundException,
    ConflictException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Product } from './product.entity';
import { CreateProductDto, UpdateProductDto } from './product.dto';
import { CategoryService } from '../category/category.service';

@Injectable()
export class ProductService {
    constructor(
        @InjectRepository(Product)
        private readonly productRepo: Repository<Product>,
        private readonly categoryService: CategoryService,
    ) { }

    async findAll(): Promise<Product[]> {
        return this.productRepo.find({
            relations: ['category'],
            order: { createdAt: 'DESC' },
        });
    }

    async findOne(id: number): Promise<Product> {
        const product = await this.productRepo.findOne({
            where: { id },
            relations: ['category'],
        });
        if (!product) throw new NotFoundException(`Produit #${id} introuvable`);
        return product;
    }

    async create(dto: CreateProductDto): Promise<Product> {
        // Vérifier si la catégorie existe
        await this.categoryService.findOne(dto.categoryId);

        // Vérifier si le SKU est unique
        const existing = await this.productRepo.findOne({ where: { sku: dto.sku } });
        if (existing) throw new ConflictException(`Le SKU "${dto.sku}" est déjà utilisé`);

        const product = this.productRepo.create(dto);
        return this.productRepo.save(product);
    }

    async update(id: number, dto: UpdateProductDto): Promise<Product> {
        const product = await this.findOne(id);

        if (dto.categoryId) {
            await this.categoryService.findOne(dto.categoryId);
        }

        if (dto.sku && dto.sku !== product.sku) {
            const existing = await this.productRepo.findOne({ where: { sku: dto.sku } });
            if (existing) throw new ConflictException(`Le SKU "${dto.sku}" est déjà utilisé`);
        }

        Object.assign(product, dto);
        return this.productRepo.save(product);
    }

    async remove(id: number): Promise<{ message: string }> {
        const product = await this.findOne(id);
        await this.productRepo.remove(product);
        return { message: `Produit #${id} supprimé` };
    }
}
