"""
Course_Recommender 알고리즘
1. Word2Vec으로 산책로 keyword 학습 진행 (이게 끝임)
"""
"""
db에 적용시킬 알고리즘
1. 유저 A의 finish_courses 테이블에서 완주한 course의 id값을 가져온다.
2. 이 id 값을 course_types 테이블에서 각각의 tag들을 찾는다.
3. 각각의 course의 tag를 다 Word2Vec으로 벡터 변환
4. 변환 시킨 벡터를 다 더하고 course의 개수로 나눔 -> 이것이 유저 A의 키워드 벡터
5. 3에서 변환한 테이블에서 A가 이용했던 id 는 제거
6. 유저 A의 키워드 벡터와 3에서 변환한 테이블에서 각 course와 코사인 유사도 값 계산
7. 코사인 유사도 점수가 높은 course id를 A한테 5개 뽑아준다.
"""

from gensim.models import Word2Vec
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import pymysql
from haversine import haversine


#d
class course_recommender():
    def __init__(self, user_id = int) :
        self.user_id = user_id # json에서 갖고온 유저의 ID

        self.model = Word2Vec.load(".\\recommender\word2vec.model") # 미리 학습한 word2Vec 모델 load

        self.best = 3 # 몇 개의 산책로를 추천할 것인지

    # 산책로 tag를 pasing 하는 함수
    def tokenizer(self, text):
        temp = text.split(" ")
        temp_list = [[single] for single in temp]
        return temp_list

    # pasing한 산책로 tag를 통해서 산책로의 vector를 생성하는 함수
    def course_vector_calculator(self, temp_list):
        course_vector = np.zeros((1,20))
        for i in temp_list :
            course_vector = course_vector + self.model.wv[i]
        course_vector = course_vector / len(temp_list)
        return course_vector

    # 두 산책로의 tag vector의 유사도를 구하는 함수
    def similarity_calculator(self, vector1, vector2):
        similarity_vector = cosine_similarity(vector1, vector2)
        similarity_score = similarity_vector.mean()
        return similarity_score

    # 산책로를 추천해 주는 함수
    def recommend(self):
        id_input = self.user_id
        
        conn = pymysql.connect(host="localhost", user="root", password="1234", db="naemansan")
        
        cursor = conn.cursor()

        # DB에서 특정 사용자가 이용한 산책로의 id와 완주 여부를 갖고온다.
        query = """
        SELECT course_id, finish_status  
        FROM using_courses 
        WHERE user_id = %d""" % (id_input) 
        cursor.execute(query)
        results = cursor.fetchall()
        number = len(results)

        # 예외처리, 이용한 산책로가 없으면 아무것도 추천을 하지 않는다.
        if number == 0 :
            return { "courseid" : []}
        
        user_vector = np.zeros((1,20)) # 특정 사용자의 vector
        course_list =[] # 특정 사용자가 이용한 산책로 ID
        finish_list =[] # 특정 사용자가 이용한 산책로의 완주여부 완주시 1, 아닐시 0
        for i in range(number):
            course_list.append(results[i][0])
            finish_list.append(results[i][1])

        # DB에서 사용자가 이용한 산책로의 tag를 읽어와서 word2Vec을 통해 vector로 변환
        # 변환한 vector를 사용자 vector에 더한 후에 총 더한 숫자로 나눈다.
        # 해당 산책로를 완주했을 시에는 한번 더 더한다.
        for idx, co_id in enumerate(course_list) :
            query = """
            SELECT tag 
            FROM course_tags 
            WHERE course_id = %d""" % (co_id)
            cursor.execute(query)
            results = cursor.fetchall()

            raw_tags = results[0][0]
            token_tags = self.tokenizer(raw_tags) # 산책로의 tag 리스트
            course_vector = self.course_vector_calculator(token_tags) # word2Vec으로 변환
            user_vector += course_vector    # status가 1이면 한번 더 더하고 number++
            if finish_list[idx] == 1 : 
                user_vector += course_vector
                number += 1
        user_vector = user_vector / number

        # 사용자 vector와 전체 산책로의 유사도 점수 계산
        query = """
        SELECT tag, course_id 
        FROM course_tags
        """ 
        cursor.execute(query)
        results = cursor.fetchall()
        candidates_score = [] # 추천이될 가능성이 있는 산책로의 유사도 점수
        candidates_id = [] # 추천이될 가능성이 있는 산책로의 ID
        for i in range(len(results)) :
            course_id = results[i][1]
            course_tag = results[i][0]
            if course_id in course_list : # 사용자가 이용했던 산책로라면 추천을 하지 않는다.
                continue
            candidates_id.append(course_id) # 추천이될 가능성이 있는 산책로의 ID와 유사도 점수를 list에 넣는다.
            candidates_score.append(self.similarity_calculator(user_vector, self.course_vector_calculator(self.tokenizer(course_tag))))
        
        if len(candidates_id) != 0 :
            temp = np.argsort(np.array(candidates_score))[::-1]
            best_courses = [] # 유사도가 높은 순으로 정렬된 list
            for i in range(len(temp)) :
                best_courses.append({"id": candidates_id[temp[i]]})
            
            # 추천하고자 하는 산책로의 수가 정렬된 산책로의 수보다 크다면 추천을 하지 않는다.
            if len(best_courses) < self.best :
                return { "courseid" : []}
            return { "courseid" : best_courses[0:self.best]} # self.best개의 산책로 ID를 유사도가 높은 순으로 반환
        
        else :
            return { "courseid" : []}
