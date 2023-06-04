import pymysql
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from haversine import haversine


class  finish_Checker():
    def __init__(self, courseid):
        self.courseid = courseid

    def calculate_Similarity(self, input_coord) :
        # 두 산책로의 유사도가 높으면 false/true를 반환 하는 함수
        token_true = {
            "success" : True
        }
        token_false = {
            "success" : False
        }

        # 정규화 (X_mean, Y_mean : [ 37.554812 126.988204] X_std, Y_std :  [0.0031548  0.00720859])
        # jupyter notebook에서 standard scaler를 이용해서 얻을 결과를 값만 사용
        X_mean, Y_mean = 37.554812, 126.988204
        X_std, Y_std = np.sqrt(0.0031548), np.sqrt(0.00720859)

        # user가 등록하고자 하는 gps의 json을 좌표형식으로 쪼갠다.
        temp = np.array(input_coord.split())
        user_input = np.array([])
        for i in temp:
            user_input = np.append(user_input, float(i))
        user_input = user_input.reshape(-1, 2)
        user_XY = tuple(np.mean(user_input, axis=0))

        # 위에서 얻은 좌표형식의 gps값을 정규화
        user_frame = pd.DataFrame(user_input)
        user_frame.iloc[:, 0] = (user_frame.iloc[:, 0] - X_mean) / X_std
        user_frame.iloc[:, 1] = (user_frame.iloc[:, 1] - Y_mean) / Y_std
        user_std = user_frame.values

        # DB연결
        conn = pymysql.connect(host="localhost", user="root", password="1234", db="naemansan")

        cursor = conn.cursor()

        # DB에서 유저가 이용한 산책로를 모두 갖고온다.
        query = """
        SELECT ST_AsText(locations) 
        FROM enrollment_courses
        WHERE id = %d""" %(self.courseid)
        cursor.execute(query)
        results = cursor.fetchall()
        
        locations_list = []  # multipoint형 리스트
        coordinates_list = []  # multipoint를 float으로 변환한 리스트

        # multipoint 저장
        for row in results:
            locations_list.append(row[0])

        # float 형태로 저장
        for row in locations_list:
            row = row.replace("MULTIPOINT", "")
            row = row.replace("(", "")
            row = row.replace(")", "")
            row = row.replace(",", " ")
            temp = row.split()
            float_coord = []
            for i in temp[::-1]:  
                float_coord.append(float(i))
            coordinates_list.append(float_coord)

        # 좌표를 데이터프레임으로 변환
        walking_Path = pd.DataFrame(coordinates_list)

        # 산책로 마다 데이터프레임으로 변환
        X, Y = walking_Path.shape
        for i in range(X):
            walking = walking_Path.iloc[i]
            walking = walking.dropna()
            walking_list = walking.values
            walking_list = walking_list.reshape(-1, 2)

            temp_frame = pd.DataFrame(walking_list)
            temp_frame = temp_frame.dropna(axis=1)

            # 정규화
            temp_frame.iloc[:, 0] = (temp_frame.iloc[:, 0] - X_mean) / X_std
            temp_frame.iloc[:, 1] = (temp_frame.iloc[:, 1] - Y_mean) / Y_std

            # 유사도 벡터와 점수
            walking_std = temp_frame.values
            DB_XY = np.mean(walking_std, axis=0)
            DB_X = DB_XY[0] * X_std + X_mean
            DB_Y = DB_XY[1] * Y_std + Y_mean
            DB_coord = (DB_X, DB_Y)
            distance = haversine(user_XY, DB_coord)
            similarity_vector = cosine_similarity(walking_std, user_std)
            similarity_score = np.mean(np.max(similarity_vector, axis=0))
            
            # threshold -> 0.8 (나중에 바뀔수도..??)
            # checker와 다르게 finisher의 경우 약간 후하게 책정
            threshold = 0.95

            # 유사도가 높으면 반복문 멈추고 등록 불가
            # 산책로의 좌표가 길면 유사도 벡터가 점점 희소해지는 문제를 벡터 열의 max값만 사용
            # 벡터 열의 max값을 이용한다는 것의 의미는 A 산책로의 한 좌표와 제일 가까운 B 산책로의 한 좌표와의 유사도를 의미
            if ((similarity_score > threshold or similarity_score < -threshold) or np.all(np.isclose(np.diag(similarity_vector), 1.0))) and distance < 0.3:
                return token_true

        return token_false


