import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:gesco_app/core/theme/app_theme.dart';
import 'package:gesco_app/core/router/router.dart';

void main() {
  runApp(
    const ProviderScope(
      child: GescoApp(),
    ),
  );
}

class GescoApp extends ConsumerWidget {
  const GescoApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
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
