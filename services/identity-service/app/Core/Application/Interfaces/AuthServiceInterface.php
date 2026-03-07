<?php

namespace App\Core\Application\Interfaces;

interface AuthServiceInterface
{
    public function authenticate(string $email, string $password): ?string;
    public function hashPassword(string $password): string;
    public function checkPassword(string $password, string $hashedPassword): bool;
}
