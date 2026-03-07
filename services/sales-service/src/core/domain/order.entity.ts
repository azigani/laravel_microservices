export enum OrderStatus {
    PENDING = 'PENDING',
    POSTED = 'POSTED',
    RESERVED = 'RESERVED',
    COMPLETED = 'COMPLETED',
    CANCELED = 'CANCELED',
    FAILED = 'FAILED',
}

export class OrderItem {
    constructor(
        public readonly productId: string,
        public readonly quantity: number,
        public readonly unitPrice: number,
    ) { }

    get total(): number {
        return this.quantity * this.unitPrice;
    }
}

export class Order {
    constructor(
        public readonly id: string,
        public readonly customerId: string,
        public readonly items: OrderItem[],
        public status: OrderStatus = OrderStatus.PENDING,
        public readonly createdAt: Date = new Date(),
    ) { }

    get totalAmount(): number {
        return this.items.reduce((sum, item) => sum + item.total, 0);
    }

    markAsReserved() {
        this.status = OrderStatus.RESERVED;
    }

    markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
    }

    cancel() {
        this.status = OrderStatus.CANCELED;
    }
}
