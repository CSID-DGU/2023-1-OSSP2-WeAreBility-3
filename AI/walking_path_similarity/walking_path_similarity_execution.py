import pymysql
from shapely import wkt
from shapely.geometry import MultiPoint
from shapely.wkb import loads
import struct
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity



# 표준화 (X_mean, Y_mean : [ 37.554812 126.988204] X_std, Y_std :  [0.0031548  0.00720859])
X_mean, Y_mean = 37.554812, 126.988204
X_std, Y_std = np.sqrt(0.0031548), np.sqrt(0.00720859)

# 유저 input(좌표의 x,y 값을 순서대로 입력한다.)
temp = input("gps 값 입력 : ")
temp2 = np.array(temp.split())
user_input = np.array([])
for i in temp2 :
    user_input = np.append(user_input, float(i))
user_input = user_input.reshape(-1, 2)


user_frame = pd.DataFrame(user_input)
user_frame.iloc[:,0] = (user_frame.iloc[:,0] - X_mean) / X_std
user_frame.iloc[:,1] = (user_frame.iloc[:,1] - Y_mean) / Y_std
print(user_frame)

# DB연결
conn = pymysql.connect(
    host='localhost',
    user='root',
    password='1234',
    db='naemansan'
)

cursor = conn.cursor()

# 좌표 정보를 db에 저장
"""coordinates = [(37.5622,126.9985)]
location = wkt.dumps(MultiPoint(coordinates))
print(type(location))
query = "INSERT INTO courses (title, start_location, locations) VALUES (%s, %s, ST_GeomFromText(%s, 4326))"
data = ("Hoin", "서울", location)
cursor.execute(query, data)
conn.commit()"""

# 좌표 정보에서 전부 갖고와서 한줄 씩 리스트에 저장
# 예를 들어 [[37.0, 127.0, 37.1, 127.1]]

query = "SELECT ST_AsText(locations) FROM courses"
cursor.execute(query)
results = cursor.fetchall()

locations_list = [] # multipoint형 리스트
coordinates_list = [] # multipoint를 float으로 변환한 리스트

# multipoint 저장
for row in results:
    locations_list.append(row[0])

# float 형태로 저장
for row in locations_list:
    row = row.replace('MULTIPOINT', '')
    row = row.replace('(', '')
    row = row.replace(')', '')
    row = row.replace(',', ' ')
    temp = row.split()
    float_coord = []
    for i in temp : 
        float_coord.append(float(i))
    coordinates_list.append(float_coord)

print("현재 리스트 : ", coordinates_list)

# 좌표를 데이터프레임으로 변환
walking_Path = pd.DataFrame(coordinates_list)
print("현재 프레임 : ", walking_Path)

# 산책로 마다 데이터프레임으로 변환
X, Y = walking_Path.shape
for i in range(X):
    walking = walking_Path.iloc[i]
    walking = walking.dropna()
    walking_list = walking.values
    walking_list = walking_list.reshape(-1,2)

    temp_frame = pd.DataFrame(walking_list)
    temp_frame = temp_frame.dropna(axis=1)

    # 정규화
    temp_frame.iloc[:,0] = (temp_frame.iloc[:,0] - X_mean) / X_std
    temp_frame.iloc[:,1] = (temp_frame.iloc[:,1] - Y_mean) / Y_std
    print(temp_frame)

    # 유사도 벡터와 점수
    similarity_vector = cosine_similarity(walking_std, user_std)
    similarity_score = similarity_vector.mean()
    print(similarity_score)
    print(cosine_similarity(walking_std, user_std))
