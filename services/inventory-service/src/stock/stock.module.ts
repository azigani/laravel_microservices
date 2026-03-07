import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { StockService } from './stock.service';
import { StockController } from './stock.controller';
import { Stock } from './stock.entity';
import { StockEventController } from './stock-event.controller';
import { ClientsModule, Transport } from '@nestjs/microservices';

@Module({
    imports: [
        TypeOrmModule.forFeature([Stock]),
        ClientsModule.register([
            {
                name: 'SALES_SERVICE',
                transport: Transport.RMQ,
                options: {
                    urls: [process.env.RABBITMQ_URL || 'amqp://guest:guest@rabbitmq:5672'],
                    queue: 'sales_queue',
                    queueOptions: {
                        durable: false,
                    },
                },
            },
        ]),
    ],
    controllers: [StockController, StockEventController],
    providers: [StockService],
    exports: [StockService],
})
export class StockModule { }
