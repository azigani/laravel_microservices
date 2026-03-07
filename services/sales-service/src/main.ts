import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { Transport } from '@nestjs/microservices';

async function bootstrap() {
    const app = await NestFactory.create(AppModule);

    // Configuration Microservice RabbitMQ
    app.connectMicroservice({
        transport: Transport.RMQ,
        options: {
            urls: [process.env.RABBITMQ_URL || 'amqp://guest:guest@rabbitmq:5672'],
            queue: 'sales_queue',
            queueOptions: {
                durable: false,
            },
        },
    });

    app.useGlobalPipes(new ValidationPipe({
        whitelist: true,
        forbidNonWhitelisted: true,
        transform: true,
    }));

    await app.startAllMicroservices();

    const port = process.env.PORT || 3002;
    await app.listen(port);
    console.log(`Sales Service is running on: http://localhost:${port} and listening to RabbitMQ`);
}
bootstrap();
