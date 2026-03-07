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
import type { Request, Response } from 'express';
import axios from 'axios';
import { ConfigService } from '@nestjs/config';
import { JwtAuthGuard } from '../auth/jwt.guard';

@Controller('sales')
@UseGuards(JwtAuthGuard)
export class SalesProxyController {
    private salesUrl: string;

    constructor(private config: ConfigService) {
        this.salesUrl = this.config.get<string>(
            'SALES_SERVICE_URL',
            'http://localhost:3002',
        );
    }

    @All('*path')
    async proxy(@Req() req: Request, @Res() res: Response, @Param('path') path: string) {
        const targetUrl = `${this.salesUrl}/${path}`;

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
                    message: 'Sales service indisponible',
                    error: error.message,
                },
                HttpStatus.BAD_GATEWAY,
            );
        }
    }
}
