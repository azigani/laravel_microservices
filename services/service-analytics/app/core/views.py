from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import DashboardStatsSerializer
from .mongodb import get_stats_collection

class DashboardStatsView(APIView):
    def get(self, request):
        collection = get_stats_collection()
        stats = collection.find_one({"_id": "global_stats"})
        
        if not stats:
            stats = {
                "total_sales": 0,
                "order_count": 0,
                "top_products": []
            }
            
        serializer = DashboardStatsSerializer({
            "total_sales": stats.get("total_sales", 0),
            "order_count": stats.get("order_count", 0),
            "top_products": stats.get("top_products", [])
        })
        return Response(serializer.data)
