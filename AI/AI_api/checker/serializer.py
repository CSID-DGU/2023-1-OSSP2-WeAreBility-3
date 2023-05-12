from rest_framework import serializers
from .models import Users

class userSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ["user_index", "userid","title","createdDateTime","introduction",
                  "coursekeyword","segmentId","startPoint","endPoint","points"]
