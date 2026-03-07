import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { OrderTypeOrm, OrderItemTypeOrm } from './infrastructure/persistence/order.orm-entity';
import { TypeOrmOrderRepository } from './infrastructure/persistence/typeorm-order.repository';
import { IOrderRepository } from './core/domain/order.repository.interface';
import { PlaceOrderUseCase } from './core/application/place-order.use-case';
import { OrderController } from './interfaces/http/order.controller';
import { IMessageBroker } from './core/domain/message-broker.interface';
import { RabbitMQMessageBroker } from './infrastructure/messaging/rabbitmq-broker';
import { ProcessStockReservedUseCase, ProcessStockUnavailableUseCase } from './core/application/saga-responses.use-case';
import { OrderSagaController } from './interfaces/http/order-saga.controller';

@Module({
    imports: [
        TypeOrmModule.forFeature([OrderTypeOrm, OrderItemTypeOrm]),
    ],
    controllers: [OrderController, OrderSagaController],
    providers: [
        PlaceOrderUseCase,
        ProcessStockReservedUseCase,
        ProcessStockUnavailableUseCase,
        {
            provide: IOrderRepository,
            useClass: TypeOrmOrderRepository,
        },
        {
            provide: IMessageBroker,
            useClass: RabbitMQMessageBroker,
        },
    ],
    exports: [PlaceOrderUseCase],
})
export class SalesModule { }
