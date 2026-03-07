import { IDomainEvent } from './events/order-placed.event';

export interface IMessageBroker {
    publish(event: IDomainEvent): Promise<void>;
}

export const IMessageBroker = Symbol('IMessageBroker');
