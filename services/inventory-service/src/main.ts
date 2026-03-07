import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { Transport } from '@nestjs/microservices';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Microservice RabbitMQ configuration
  app.connectMicroservice({
    transport: Transport.RMQ,
    options: {
      urls: [process.env.RABBITMQ_URL || 'amqp://guest:guest@rabbitmq:5672'],
      queue: 'inventory_queue',
      queueOptions: {
        durable: false,
      },
    },
  });

  app.enableCors({ origin: '*' });
  app.useGlobalPipes(new ValidationPipe({ whitelist: true, transform: true }));

  await app.startAllMicroservices();

  const port = process.env.PORT ?? 3001;
  await app.listen(port);
  console.log(`📦 Inventory Service running on http://localhost:${port} and listening to RabbitMQ`);
}
bootstrap();
