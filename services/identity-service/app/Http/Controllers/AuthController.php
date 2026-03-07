<?php

namespace App\Http\Controllers;

use App\Core\Application\UseCases\AuthenticateUserUseCase;
use App\Core\Application\UseCases\RegisterUserUseCase;
use Illuminate\Http\Request;
use Illuminate\Routing\Controller;

class AuthController extends Controller
{
    private RegisterUserUseCase $registerUseCase;
    private AuthenticateUserUseCase $authenticateUseCase;

    public function __construct(
        RegisterUserUseCase $registerUseCase,
        AuthenticateUserUseCase $authenticateUseCase
    ) {
        $this->registerUseCase = $registerUseCase;
        $this->authenticateUseCase = $authenticateUseCase;
    }

    public function register(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:6',
        ]);

        try {
            $userDto = $this->registerUseCase->execute($request->all());
            return response()->json($userDto, 201);
        } catch (\Exception $e) {
            return response()->json(['error' => $e->getMessage()], 400);
        }
    }

    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|string|email',
            'password' => 'required|string',
        ]);

        try {
            $result = $this->authenticateUseCase->execute(
                $request->input('email'),
                $request->input('password')
            );
            return response()->json($result);
        } catch (\Exception $e) {
            return response()->json(['error' => $e->getMessage()], 401);
        }
    }

    public function me()
    {
        return response()->json(auth()->user());
    }

    public function logout()
    {
        auth()->logout();
        return response()->json(['message' => 'Successfully logged out']);
    }
}
