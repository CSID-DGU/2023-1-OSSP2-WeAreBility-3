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
from gensim.models import KeyedVectors
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# 산책로 tag를 pasing 하는 함수
def tokenizer(text):
    temp = text.split(" ")
    temp_list = [[single] for single in temp]
    return temp_list

# pasing한 산책로 tag를 통해서 산책로의 vector를 생성하는 함수
def course_vector_calculator(temp_list):
    course_vector = np.zeros((1,20))
    for i in temp_list :
        course_vector = course_vector + model.wv[i]
    course_vector = course_vector / len(temp_list)
    return course_vector

def similarity_calculator(vector1, vector2):
    similarity_vector = cosine_similarity(vector1, vector2)
    similarity_score = similarity_vector.mean()
    return similarity_vector, similarity_score


"""
 이부분은 load를 하기 때문에 필요가 없음
keyword_raw = 힐링 스타벅스 자연 오솔길 도심 출근길 퇴근길 점심시간 스트레스해소
한강 공원 성수 강아지 바다 해안가 러닝 맛집 카페 영화 문화 사색
핫플 서울숲 경복궁 한옥마을 문화재 고양이 개울가 계곡 들판 산 동산 야경 노을 숲길
강서구 양천구 구로구 영등포구 금천구 동작구 관악구 서초구 강남구 송파구 강동구
은평구 서대문구 마포구 용산구 중구 종로구 도봉구 강북구 성북구 동대문구 성동구 노원구
중랑구 광진구
keyword = keyword_raw.replace("\n", " ")
keyword = keyword.split(" ")
keyword_list = [[single] for single in keyword]
model = Word2Vec(sentences = keyword_list, vector_size=20, window=1, min_count=1, workers=4)
model.save("word2vec.model")

"""


model = Word2Vec.load("C:\Hoin666\\2023-1-OSSP2-WeAreBility-3\AI\course_recommender\word2vec.model")

example1 = "힐링 스타벅스 자연"
example1_vector = course_vector_calculator(tokenizer(example1))
print(example1_vector)

example2 = "스타벅스 힐링 자연"
example2_vecotr = course_vector_calculator(tokenizer(example2))
print(example2_vecotr)

vector, score = similarity_calculator(example1_vector, example2_vecotr)
print("유사도 vector : ", vector)
print("점수 : ", score)
