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

@Controller('analytics')
@UseGuards(JwtAuthGuard)
export class AnalyticsProxyController {
    private analyticsUrl: string;

    constructor(private config: ConfigService) {
        this.analyticsUrl = this.config.get<string>(
            'ANALYTICS_SERVICE_URL',
            'http://localhost:8086',
        );
    }

    @All('*path')
    async proxy(@Req() req: Request, @Res() res: Response, @Param('path') path: string) {
        const targetUrl = `${this.analyticsUrl}/${path}`;

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
                    message: 'Analytics service indisponible',
                    error: error.message,
                },
                HttpStatus.BAD_GATEWAY,
            );
        }
    }
}
