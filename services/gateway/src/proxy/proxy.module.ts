import { Module } from '@nestjs/common';
import { InventoryProxyController } from './inventory.proxy.controller';
import { AuthProxyController } from './auth.proxy.controller';
import { SalesProxyController } from './sales.proxy.controller';
import { APP_GUARD } from '@nestjs/core';
import { JwtAuthGuard } from '../auth/jwt.guard';

@Module({
    controllers: [InventoryProxyController, AuthProxyController, SalesProxyController],
    providers: [
        {
            provide: APP_GUARD,
            useClass: JwtAuthGuard,
        },
    ],
})
export class ProxyModule { }
