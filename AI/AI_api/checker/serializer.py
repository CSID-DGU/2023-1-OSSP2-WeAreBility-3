from rest_framework import serializers
from .models import Users

class userSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ["id", "userid","title","createdDateTime","introduction",
                  "courseTags","startLocationName","locations"]
