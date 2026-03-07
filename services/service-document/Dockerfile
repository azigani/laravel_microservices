FROM php:8.3-fpm-alpine

WORKDIR /var/www

RUN apk add --no-cache \
    curl \
    libpng-dev \
    libxml2-dev \
    zip \
    unzip

RUN docker-php-ext-install pdo pdo_mysql gd

COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

COPY . .

RUN composer install --no-dev --optimize-autoloader

EXPOSE 8002
CMD ["php", "artisan", "serve", "--host=0.0.0.0", "--port=8002"]
