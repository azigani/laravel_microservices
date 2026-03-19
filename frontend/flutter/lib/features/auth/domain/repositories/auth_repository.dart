import '../entities/user.dart';

abstract class IAuthRepository {
  Future<User> login(String email, String password);
  Future<User?> tryAutoLogin();
  Future<void> logout();
}
