import { Injectable, OnModuleInit, OnModuleDestroy, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Eureka } from 'eureka-js-client';

@Injectable()
export class EurekaService implements OnModuleInit, OnModuleDestroy {
    private client: Eureka;
    private readonly logger = new Logger(EurekaService.name);

    constructor(private configService: ConfigService) {
        const hostName = this.configService.get<string>('HOSTNAME') || 'inventory-service';
        const ipAddr = this.configService.get<string>('IP_ADDRESS') || 'inventory-service';
        const port = parseInt(this.configService.get<string>('PORT') || '3002', 10);
        const eurekaHost = this.configService.get<string>('EUREKA_HOST') || 'discovery-server';
        const eurekaPort = parseInt(this.configService.get<string>('EUREKA_PORT') || '8761', 10);

        this.client = new Eureka({
            instance: {
                app: 'inventory-service',
                hostName: hostName,
                ipAddr: ipAddr,
                port: {
                    '$': port,
                    '@enabled': true,
                },
                vipAddress: 'inventory-service',
                dataCenterInfo: {
                    '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                    name: 'MyOwn',
                },
            },
            eureka: {
                host: eurekaHost,
                port: eurekaPort,
                servicePath: '/eureka/apps/',
            },
        });
    }

    onModuleInit() {
        this.client.start((error) => {
            if (error) {
                this.logger.error('Error starting Eureka client', error);
            } else {
                this.logger.log('Inventory service registered with Eureka');
            }
        });
    }

    onModuleDestroy() {
        this.client.stop((error) => {
            if (error) {
                this.logger.error('Error stopping Eureka client', error);
            } else {
                this.logger.log('Inventory service deregistered from Eureka');
            }
        });
    }
}
