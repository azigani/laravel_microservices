<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\DocumentController;

Route::prefix('documents')->group(function () {
    Route::post('/generate', 'App\Http\Controllers\DocumentController@generate');
    Route::get('/{id}', 'App\Http\Controllers\DocumentController@show');
});
