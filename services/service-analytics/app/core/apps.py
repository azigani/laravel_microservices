from django.apps import AppConfig
import os

class CoreConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'app.core'

    def ready(self):
        # Start the RabbitMQ consumer in a background thread
        # only if we're not in the main thread (to avoid double start in dev server)
        if os.environ.get('RUN_MAIN') == 'true':
            from .messaging import run_consumer_async
            run_consumer_async()
