import {
    Injectable,
    NotFoundException,
    ConflictException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Category } from './category.entity';
import { CreateCategoryDto, UpdateCategoryDto } from './category.dto';

@Injectable()
export class CategoryService {
    constructor(
        @InjectRepository(Category)
        private readonly categoryRepo: Repository<Category>,
    ) { }

    findAll(): Promise<Category[]> {
        return this.categoryRepo.find({ order: { name: 'ASC' } });
    }

    async findOne(id: number): Promise<Category> {
        const category = await this.categoryRepo.findOne({ where: { id } });
        if (!category) throw new NotFoundException(`Catégorie #${id} introuvable`);
        return category;
    }

    async create(dto: CreateCategoryDto): Promise<Category> {
        const existing = await this.categoryRepo.findOne({ where: { name: dto.name } });
        if (existing) throw new ConflictException(`Catégorie "${dto.name}" existe déjà`);
        const category = this.categoryRepo.create(dto);
        return this.categoryRepo.save(category);
    }

    async update(id: number, dto: UpdateCategoryDto): Promise<Category> {
        const category = await this.findOne(id);
        Object.assign(category, dto);
        return this.categoryRepo.save(category);
    }

    async remove(id: number): Promise<{ message: string }> {
        const category = await this.findOne(id);
        await this.categoryRepo.remove(category);
        return { message: `Catégorie #${id} supprimée` };
    }
}
