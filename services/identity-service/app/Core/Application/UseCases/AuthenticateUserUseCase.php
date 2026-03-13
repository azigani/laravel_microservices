<?php

namespace App\Core\Application\UseCases;

use App\Core\Application\Interfaces\AuthServiceInterface;
use App\Core\Application\DTOs\UserDTO;
use App\Core\Domain\Repositories\UserRepositoryInterface;

class AuthenticateUserUseCase
{
    private UserRepositoryInterface $userRepository;
    private AuthServiceInterface $authService;

    public function __construct(UserRepositoryInterface $userRepository, AuthServiceInterface $authService)
    {
        $this->userRepository = $userRepository;
        $this->authService = $authService;
    }

    public function execute(string $email, string $password): array
    {
        $user = $this->userRepository->findByEmail($email);

        if (!$user || !$this->authService->checkPassword($password, $user->getPassword())) {
            throw new \Exception("Invalid credentials");
        }

        if (!$user->is_active) {
            throw new \Exception("Your account is deactivated");
        }

        $token = $this->authService->authenticate($email, $password);
        
        if (!$token) {
            throw new \Exception("Could not create token");
        }

        // Audit Log
        try {
            \Illuminate\Support\Facades\Http::post('http://service-audit:8086/api/audit', [
                'userId' => (string)$user->id,
                'action' => 'USER_LOGIN',
                'service' => 'identity-service',
                'resource' => 'User',
                'resourceId' => (string)$user->id,
                'metadata' => ['email' => $email]
            ]);
        } catch (\Exception $e) {
            // Log error but don't break authentication
            \Illuminate\Support\Facades\Log::error("Failed to send audit log: " . $e->getMessage());
        }

        return [
            'user' => UserDTO::fromEntity($user),
            'token' => $token
        ];
    }
}
