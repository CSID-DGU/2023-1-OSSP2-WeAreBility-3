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


#d
class course_recommender():
    def __init__(self, user_id = int) :
        self.user_id = user_id

        self.model = Word2Vec.load(".\\recommender\word2vec.model")

        self.best = 3

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

    def similarity_calculator(self, vector1, vector2):
        similarity_vector = cosine_similarity(vector1, vector2)
        similarity_score = similarity_vector.mean()
        return similarity_score

    def recommend(self):
        id_input = self.user_id
        

        # db읽기 -1 finish table 가져오기
        conn = pymysql.connect(host="localhost", user="root", password="1234", db="naemansan")
        
        cursor = conn.cursor()
        query = """
        SELECT course_id, finish_status  
        FROM using_courses 
        WHERE user_id = %d""" % (id_input) 
        cursor.execute(query)
        results = cursor.fetchall()
        number = len(results)

        if number == 0 :
            return { "courseid" : []}


    # db읽기 -2 user_id에 담긴 정보로 tag 읽어서 vector 만들기
        user_vector = np.zeros((1,20))
        course_list =[]
        finish_list =[]
        for i in range(number):
            course_list.append(results[i][0])
            finish_list.append(results[i][1])

        # 유저 vector 계산
        for idx, co_id in enumerate(course_list) :
            query = """
            SELECT tag 
            FROM course_tags 
            WHERE course_id = %d""" % (co_id)  # status또한 fetch
            cursor.execute(query)
            results = cursor.fetchall()

            raw_tags = results[0][0]
            token_tags = self.tokenizer(raw_tags)
            course_vector = self.course_vector_calculator(token_tags)
            user_vector += course_vector    # status가 1이면 한번 더 더하고 number++
            if finish_list[idx] == 1 : #
                user_vector += course_vector
                number += 1
        user_vector = user_vector / number

        # 유저 vector와 전체 course tag의 유사도 점수 계산

        query = """
        SELECT tag, course_id 
        FROM course_tags
        """ 
        cursor.execute(query)
        results = cursor.fetchall()
        candidates_score = []
        candidates_id = []
        for i in range(len(results)) :
            course_id = results[i][1]
            course_tag = results[i][0]
            if course_id in course_list :
                continue
            candidates_id.append(course_id)
            candidates_score.append(self.similarity_calculator(user_vector, self.course_vector_calculator(self.tokenizer(course_tag))))
        

        if len(candidates_id) != 0 :
            temp = np.argsort(np.array(candidates_score))[::-1]
            best_courses = []
            for i in range(len(temp)) :
                best_courses.append({"id": candidates_id[temp[i]]})
            
            return { "courseid" : best_courses[0:self.best]}
        
        else :
            return { "courseid" : []}
