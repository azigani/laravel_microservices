import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    CreateDateColumn,
    UpdateDateColumn,
    OneToOne,
    JoinColumn,
} from 'typeorm';
import { Product } from '../product/product.entity';

@Entity('stocks')
export class Stock {
    @PrimaryGeneratedColumn()
    id: number;

    @Column('int', { default: 0 })
    quantity: number;

    @Column({ nullable: true })
    location: string;

    @OneToOne(() => Product, { onDelete: 'CASCADE' })
    @JoinColumn()
    product: Product;

    @Column()
    productId: number;

    @CreateDateColumn()
    createdAt: Date;

    @UpdateDateColumn()
    updatedAt: Date;
}
