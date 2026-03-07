from pymongo import MongoClient
import os

class MongoManager:
    _instance = None

    @classmethod
    def get_db(cls):
        if cls._instance is None:
            uri = os.getenv('MONGO_URI', 'mongodb://mongodb:27017/')
            client = MongoClient(uri)
            cls._instance = client['gesco_analytics']
        return cls._instance

def get_stats_collection():
    return MongoManager.get_db()['dashboard_stats']
