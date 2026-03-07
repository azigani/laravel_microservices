<?php

namespace App\Infrastructure\Persistence\Eloquent;

use App\Core\Domain\Entities\User as DomainUser;
use App\Core\Domain\Repositories\UserRepositoryInterface;
use App\Models\User as EloquentUser;

class EloquentUserRepository implements UserRepositoryInterface
{
    public function findByEmail(string $email): ?DomainUser
    {
        $eloquentUser = EloquentUser::where('email', $email)->first();
        return $eloquentUser ? $this->toDomain($eloquentUser) : null;
    }

    public function findById(int $id): ?DomainUser
    {
        $eloquentUser = EloquentUser::find($id);
        return $eloquentUser ? $this->toDomain($eloquentUser) : null;
    }

    public function save(DomainUser $user): DomainUser
    {
        $eloquentUser = EloquentUser::updateOrCreate(
            ['email' => $user->getEmail()],
            [
                'name' => $user->getName(),
                'password' => $user->getPassword(),
                'roles' => json_encode($user->getRoles()),
            ]
        );

        return $this->toDomain($eloquentUser);
    }

    private function toDomain(EloquentUser $eloquentUser): DomainUser
    {
        return new DomainUser(
            $eloquentUser->id,
            $eloquentUser->name,
            $eloquentUser->email,
            $eloquentUser->password,
            is_array($eloquentUser->roles) ? $eloquentUser->roles : json_decode($eloquentUser->roles ?? '["user"]', true)
        );
    }
}
