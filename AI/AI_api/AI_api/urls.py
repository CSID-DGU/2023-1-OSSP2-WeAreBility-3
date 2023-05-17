from django.contrib import admin
from django.urls import path,include
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
	path('checker/', include('checker.urls')),
    path('recommender/', include('recommender.urls')),
    path('admin/', admin.site.urls),
]