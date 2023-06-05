from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from .walking_course_recommender import course_recommender # 이부분 수정


@csrf_exempt
def user_list(request):
    if request.method == 'POST': # POST방식일 때
        data = JSONParser().parse(request) # 요청들어온 데이터를 JSON 타입으로 파싱

        userid_data = data["userid"] # JSON에서 사용자의 id를 갖고온다.
        recommender = course_recommender(userid_data)
        return JsonResponse(recommender.recommend(), safe=False)
