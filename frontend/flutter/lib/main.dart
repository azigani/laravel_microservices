import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'core/router/router.dart';
import 'core/theme/app_theme.dart';
import 'features/auth/presentation/providers/auth_controller.dart';

void main() {
  runApp(
    const ProviderScope(
      child: GescoApp(),
    ),
  );
}

class GescoApp extends ConsumerStatefulWidget {
  const GescoApp({super.key});

  @override
  ConsumerState<GescoApp> createState() => _GescoAppState();
}

class _GescoAppState extends ConsumerState<GescoApp> {
  @override
  void initState() {
    super.initState();
    // Try auto-login on startup
    Future.microtask(() {
      ref.read(authControllerProvider.notifier).tryAutoLogin();
    });
  }

  @override
  Widget build(BuildContext context) {
    final router = ref.watch(routerProvider);

    return MaterialApp.router(
      title: 'GESCO ERP',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.system,
      routerConfig: router,
    );
  }
}
