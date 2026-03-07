<?php

namespace App\Core\Domain\Entities;

class User
{
    private ?int $id;
    private string $name;
    private string $email;
    private string $password;
    private array $roles;

    public function __construct(?int $id, string $name, string $email, string $password, array $roles = ['user'])
    {
        $this->id = $id;
        $this->name = $name;
        $this->email = $email;
        $this->password = $password;
        $this->roles = $roles;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function getEmail(): string
    {
        return $this->email;
    }

    public function getPassword(): string
    {
        return $this->password;
    }

    public function getRoles(): array
    {
        return $this->roles;
    }

    public function setPassword(string $password): void
    {
        $this->password = $password;
    }
}
