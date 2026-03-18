import os

dirs = [
    'lib/core/network',
    'lib/core/error',
    'lib/core/theme',
    'lib/core/router',
    'lib/shared',
    'lib/features/auth/data',
    'lib/features/auth/domain',
    'lib/features/auth/presentation',
    'lib/features/system/data',
    'lib/features/system/domain',
    'lib/features/system/presentation'
]

working_dir = r'c:\Users\lenovo\Documents\Laramel_microservices\frontend\flutter'

for d in dirs:
    full_path = os.path.join(working_dir, d.replace('/', os.sep))
    os.makedirs(full_path, exist_ok=True)
    print(f"Created: {full_path}")

print("Directory structure setup complete.")
