from django.urls import path, include
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
from checker import views

urlpatterns = [
    path('', views.user_list, name="user_list"),

]
