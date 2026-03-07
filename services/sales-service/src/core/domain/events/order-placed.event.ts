export interface IDomainEvent {
    occurredAt: Date;
    eventName: string;
}

export class OrderPlacedEvent implements IDomainEvent {
    public readonly occurredAt: Date;
    public readonly eventName: string = 'order.placed';

    constructor(
        public readonly orderId: string,
        public readonly customerId: string,
        public readonly items: { productId: string; quantity: number }[],
        public readonly totalAmount: number,
    ) {
        this.occurredAt = new Date();
    }
}
