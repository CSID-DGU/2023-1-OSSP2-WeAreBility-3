from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from .walking_path_similarity_execution import similarity_Checker


@csrf_exempt
def user_list(request):
    if request.method == 'GET': # GET방식일 때
        data = JSONParser().parse(request) # 요청들어온 데이터를 JSON 타입으로 파싱

        locations_data = data["pointDtos"]
        points = ""
        for i in locations_data :
            points += str(i["latitude"]) + " "
            points += str(i["longitude"]) + " "

        checker = similarity_Checker()
        return JsonResponse(checker.calculate_Similarity(points), safe=False)

        
        "checker.calculate_Similarity(user.points)"
        """if serializer.is_valid(): # 생성한 모델과 일치하면
            serializer.save() # 데이터 저장
            return JsonResponse(serializer.data, status=201) # 정상 응답 201
        return JsonResponse(serializer.errors, status=400) # 모델에 일치하지 않는 데이터일 경우"""