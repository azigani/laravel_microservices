from rest_framework import serializers

class DashboardStatsSerializer(serializers.Serializer):
    total_sales = serializers.DecimalField(max_digits=15, decimal_places=2)
    order_count = serializers.IntegerField()
    top_products = serializers.ListField(child=serializers.CharField())
