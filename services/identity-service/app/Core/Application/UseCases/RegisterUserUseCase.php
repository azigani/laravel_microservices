<?php

namespace App\Core\Application\UseCases;

use App\Core\Application\Interfaces\AuthServiceInterface;
use App\Core\Application\DTOs\UserDTO;
use App\Core\Domain\Entities\User;
use App\Core\Domain\Repositories\UserRepositoryInterface;

class RegisterUserUseCase
{
    private UserRepositoryInterface $userRepository;
    private AuthServiceInterface $authService;

    public function __construct(UserRepositoryInterface $userRepository, AuthServiceInterface $authService)
    {
        $this->userRepository = $userRepository;
        $this->authService = $authService;
    }

    public function execute(array $data): UserDTO
    {
        if ($this->userRepository->findByEmail($data['email'])) {
            throw new \Exception("User already exists");
        }

        $user = new User(
            null,
            $data['name'],
            $data['email'],
            $this->authService->hashPassword($data['password']),
            $data['roles'] ?? ['user']
        );

        $savedUser = $this->userRepository->save($user);

        return UserDTO::fromEntity($savedUser);
    }
}
