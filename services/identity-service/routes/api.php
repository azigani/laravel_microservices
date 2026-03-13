<?php

use App\Http\Controllers\AuthController;
use Illuminate\Support\Facades\Route;

Route::group([
    'prefix' => 'auth'
], function ($router) {
    Route::post('register', [AuthController::class, 'register']);
    Route::post('login', [AuthController::class, 'login']);
    Route::post('logout', [AuthController::class, 'logout'])->middleware('auth:api');
    Route::get('me', [AuthController::class, 'me'])->middleware('auth:api');
});

Route::group([
    'prefix' => 'profile',
    'middleware' => 'auth:api'
], function ($router) {
    Route::get('/', [\App\Http\Controllers\ProfileController::class, 'show']);
    Route::put('/', [\App\Http\Controllers\ProfileController::class, 'update']);
});

Route::group([
    'prefix' => 'admin',
    'middleware' => ['auth:api', 'role:admin']
], function ($router) {
    Route::get('users', [\App\Http\Controllers\UserController::class, 'index']);
    Route::get('users/{user}', [\App\Http\Controllers\UserController::class, 'show']);
    Route::put('users/{user}', [\App\Http\Controllers\UserController::class, 'update']);
    Route::patch('users/{user}/toggle', [\App\Http\Controllers\UserController::class, 'toggleStatus']);
    Route::delete('users/{user}', [\App\Http\Controllers\UserController::class, 'destroy']);
});

