<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Routing\Controller;

class ProfileController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth:api');
    }

    /**
     * Get the authenticated user's profile.
     */
    public function show(Request $request)
    {
        return response()->json([
            'user' => $request->user(),
            'roles' => $request->user()->roles ?? ['user'],
            'permissions' => $this->getPermissionsForUser($request->user())
        ]);
    }

    /**
     * Update the authenticated user's profile.
     */
    public function update(Request $request)
    {
        $user = $request->user();
        
        $validated = $request->validate([
            'name' => 'sometimes|string|max:255',
            'email' => 'sometimes|string|email|max:255|unique:users,email,' . $user->id,
        ]);

        $user->update($validated);

        return response()->json([
            'message' => 'Profile updated successfully',
            'user' => $user
        ]);
    }

    private function getPermissionsForUser($user)
    {
        // Simple logic for "Wahou" effect
        if (in_array('admin', $user->roles ?? [])) {
            return ['all'];
        }
        return ['read_sales', 'create_sales', 'read_inventory'];
    }
}
