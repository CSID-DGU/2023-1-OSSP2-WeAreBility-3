from django.db import models

# Create your models here.

class Users(models.Model):

    user_id = models.IntegerField(default= True)
    userid = models.CharField(max_length=50)
    title = models.CharField(max_length=50)
    createdDateTime = models.CharField(max_length=50)
    introduction = models.CharField(max_length=999)
    courseTags = models.CharField(max_length=300)
    startLocationName = models.CharField(max_length=100)
    locations = models.CharField(max_length=9999)

    # 5-12 db body 구체화
    
