from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from .walking_finish_checker import finish_Checker


@csrf_exempt
def finisher_list(request):
    if request.method == 'GET': # GET방식일 때
        data = JSONParser().parse(request) # 요청들어온 데이터를 JSON 타입으로 파싱

        locations_data = data["pointDtos"]
        courseid_data = data["courseid"]
        points = ""
        for i in locations_data :
            points += str(i["latitude"]) + " "
            points += str(i["longitude"]) + " "

        finisher = finish_Checker(courseid_data)
        return JsonResponse(finisher.calculate_Similarity(points), safe=False)
