import {
    Injectable,
    NestInterceptor,
    ExecutionContext,
    CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import axios from 'axios';

@Injectable()
export class AuditInterceptor implements NestInterceptor {
    intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
        const request = context.switchToHttp().getRequest();
        const { method, url, body, user } = request;
        const startTime = Date.now();

        return next.handle().pipe(
            tap(() => {
                const duration = Date.now() - startTime;
                // Only audit mutations (POST, PUT, DELETE) or interesting paths
                if (['POST', 'PUT', 'DELETE'].includes(method)) {
                    axios.post('http://service-audit:8086/api/audit', {
                        userId: user?.sub || 'GUEST',
                        action: `${method}_REQUEST`,
                        service: 'api-gateway',
                        resource: url,
                        resourceId: body?.id || body?.productId || 'N/A',
                        metadata: {
                            method,
                            duration: `${duration}ms`,
                            ip: request.ip,
                        },
                    }).catch(err => console.error('Failed to send audit from gateway', err.message));
                }
            }),
        );
    }
}
