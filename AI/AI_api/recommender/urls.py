from django.urls import path, include
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
from recommender import views # c

urlpatterns = [
    path('', views.user_list, name="user_list"),
]
