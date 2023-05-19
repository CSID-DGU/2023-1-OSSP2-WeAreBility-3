from django.urls import path, include
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
from finisher import views

urlpatterns = [
    path('', views.finisher_list, name="finisher_list"),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
