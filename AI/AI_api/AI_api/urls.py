from django.contrib import admin
from django.urls import path,include
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
	path('checker/', include('checker.urls')), # checker API url을 이용할 수 있도록 include
    path('recommender/', include('recommender.urls')),  # recommender API url을 이용할 수 있도록 include
    path('finisher/', include('finisher.urls')),  # finisher API url을 이용할 수 있도록 include
    path('admin/', admin.site.urls), # admin 페이지 url
]