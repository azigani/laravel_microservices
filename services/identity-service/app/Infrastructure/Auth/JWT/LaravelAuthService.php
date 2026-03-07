<?php

namespace App\Infrastructure\Auth\JWT;

use App\Core\Application\Interfaces\AuthServiceInterface;
use Illuminate\Support\Facades\Hash;
use Tymon\JWTAuth\Facades\JWTAuth;

class LaravelAuthService implements AuthServiceInterface
{
    public function authenticate(string $email, string $password): ?string
    {
        $token = JWTAuth::attempt(['email' => $email, 'password' => $password]);
        return $token ?: null;
    }

    public function hashPassword(string $password): string
    {
        return Hash::make($password);
    }

    public function checkPassword(string $password, string $hashedPassword): bool
    {
        return Hash::check($password, $hashedPassword);
    }
}
