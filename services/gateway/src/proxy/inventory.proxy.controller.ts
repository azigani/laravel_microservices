import {
    Controller,
    All,
    Req,
    Res,
    Param,
    UseGuards,
    HttpException,
    HttpStatus,
} from '@nestjs/common';
import { Request, Response } from 'express';
import axios from 'axios';
import { ConfigService } from '@nestjs/config';
import { JwtAuthGuard } from '../auth/jwt.guard';
import { Public } from '../auth/public.decorator';

@Controller('inventory')
@UseGuards(JwtAuthGuard)
export class InventoryProxyController {
    private inventoryUrl: string;

    constructor(private config: ConfigService) {
        this.inventoryUrl = this.config.get<string>(
            'INVENTORY_SERVICE_URL',
            'http://localhost:3001',
        );
    }

    @All('*path')
    async proxy(@Req() req: Request, @Res() res: Response, @Param('path') path: string) {
        const targetUrl = `${this.inventoryUrl}/${path}`;

        try {
            const response = await axios({
                method: req.method as any,
                url: targetUrl,
                headers: {
                    ...req.headers,
                    host: undefined,
                    'x-forwarded-for': req.ip,
                    'x-gateway': 'laramel-gateway',
                },
                data: req.body,
                params: req.query,
                validateStatus: () => true,
            });

            res.status(response.status).json(response.data);
        } catch (error) {
            throw new HttpException(
                {
                    message: 'Inventory service indisponible',
                    error: error.message,
                },
                HttpStatus.BAD_GATEWAY,
            );
        }
    }
}
