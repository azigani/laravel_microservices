import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../datasources/auth_remote_data_source.dart';
import '../../domain/entities/user.dart';
import '../../domain/repositories/auth_repository.dart';

class AuthRepositoryImpl implements IAuthRepository {
  final AuthRemoteDataSource _remoteDataSource;
  final FlutterSecureStorage _storage = const FlutterSecureStorage();
  static const String _tokenKey = 'auth_token';

  AuthRepositoryImpl(this._remoteDataSource);

  @override
  Future<User> login(String email, String password) async {
    final response = await _remoteDataSource.login(email, password);
    await _storage.write(key: _tokenKey, value: response.token);
    return response.user;
  }

  @override
  Future<User?> tryAutoLogin() async {
    final token = await _storage.read(key: _tokenKey);
    if (token == null) return null;
    
    try {
      return await _remoteDataSource.getMe(token);
    } catch (e) {
      await _storage.delete(key: _tokenKey);
      return null;
    }
  }

  @override
  Future<void> logout() async {
    await _storage.delete(key: _tokenKey);
  }
}
