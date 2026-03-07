<?php

namespace App\Providers;

use App\Core\Application\Interfaces\AuthServiceInterface;
use App\Infrastructure\Auth\JWT\LaravelAuthService;
use Illuminate\Support\ServiceProvider;

class AuthServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(AuthServiceInterface::class, LaravelAuthService::class);
    }

    public function boot(): void
    {
        //
    }
}
