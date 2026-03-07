<?php

namespace App\Core\Domain\Repositories;

use App\Core\Domain\Entities\User;

interface UserRepositoryInterface
{
    public function findByEmail(string $email): ?User;
    public function save(User $user): User;
    public function findById(int $id): ?User;
}
