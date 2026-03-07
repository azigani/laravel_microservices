<?php

namespace App\Core\Application\DTOs;

class UserDTO
{
    public function __construct(
        public ?int $id,
        public string $name,
        public string $email,
        public array $roles
    ) {}

    public static function fromEntity($user): self
    {
        return new self(
            $user->getId(),
            $user->getName(),
            $user->getEmail(),
            $user->getRoles()
        );
    }
}
