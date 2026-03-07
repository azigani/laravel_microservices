import {
    Controller,
    All,
    Req,
    Res,
    Param,
    HttpException,
    HttpStatus,
} from '@nestjs/common';
import type { Request, Response } from 'express';
import axios from 'axios';
import { ConfigService } from '@nestjs/config';
import { Public } from '../auth/public.decorator';

@Controller('auth')
export class AuthProxyController {
    private identityUrl: string;

    constructor(private config: ConfigService) {
        this.identityUrl = this.config.get<string>(
            'IDENTITY_SERVICE_URL',
            'http://localhost:8000',
        );
    }

    @Public()
    @All('*path')
    async proxy(@Req() req: Request, @Res() res: Response, @Param('path') path: string) {
        // Handle routes that don't need 'auth' prefix in the backend if necessary
        // In our Laravel setup, we used Route::group(['prefix' => 'auth']) in api.php
        // and Laravel adds /api prefix by default for api routes.
        // So the full URL is http://identity-service:8000/api/auth/...

        const targetUrl = `${this.identityUrl}/api/auth/${path}`;

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
                    message: 'Identity service indisponible',
                    error: error.message,
                },
                HttpStatus.BAD_GATEWAY,
            );
        }
    }
}
