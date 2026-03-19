import 'package:dio/dio.dart';
import '../domain/entities/user.dart';

class AuthResponse {
  final User user;
  final String token;

  AuthResponse({required this.user, required this.token});

  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    return AuthResponse(
      user: User.fromJson(json['user']),
      token: json['access_token'] ?? json['token'],
    );
  }
}

class AuthRemoteDataSource {
  final Dio _dio;

  AuthRemoteDataSource(this._dio);

  Future<AuthResponse> login(String email, String password) async {
    try {
      final response = await _dio.post('/auth/login', data: {
        'email': email,
        'password': password,
      });
      return AuthResponse.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data['message'] ?? 'Erreur lors de la connexion');
    }
  }

  Future<User> getMe(String token) async {
    try {
      final response = await _dio.get('/auth/me', 
        options: Options(headers: {'Authorization': 'Bearer $token'})
      );
      return User.fromJson(response.data);
    } on DioException catch (e) {
      throw Exception(e.response?.data['message'] ?? 'Erreur lors de la récupération du profil');
    }
  }
}
