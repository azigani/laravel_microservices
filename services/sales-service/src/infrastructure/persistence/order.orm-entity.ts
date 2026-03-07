import { Entity, PrimaryColumn, Column, CreateDateColumn, OneToMany } from 'typeorm';
import { OrderStatus } from '../../core/domain/order.entity';

@Entity('order_items')
export class OrderItemTypeOrm {
    @PrimaryColumn()
    id: string;

    @Column()
    orderId: string;

    @Column()
    productId: string;

    @Column('int')
    quantity: number;

    @Column('decimal')
    unitPrice: number;
}

@Entity('orders')
export class OrderTypeOrm {
    @PrimaryColumn()
    id: string;

    @Column()
    customerId: string;

    @Column({
        type: 'enum',
        enum: OrderStatus,
        default: OrderStatus.PENDING,
    })
    status: OrderStatus;

    @Column('decimal', { default: 0 })
    totalAmount: number;

    @CreateDateColumn()
    createdAt: Date;
}
