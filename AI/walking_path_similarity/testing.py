
import pymysql
from shapely import wkt
from shapely.geometry import MultiPoint
from shapely.wkb import loads
import struct
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity


# Example byte sequence
conn = pymysql.connect(host="localhost", user="root", password="1234", db="naemansan")

cursor = conn.cursor()


coordinates = [(37.5861, 127.019),(37.588, 127.0179),(37.5886, 127.0187),
               (37.591, 127.0151),(37.591, 127.0129),(37.5877, 127.0081),(37.5881, 127.0067)]
coordinates_start = [(37.5555, 126.8998)]
location_start = wkt.dumps(MultiPoint(coordinates_start))
location = wkt.dumps(MultiPoint(coordinates))
print(location)
print(location_start)
query = """
    INSERT INTO courses (id, user_id, title, created_date, introduction, start_location_name, locations, distance, status) 
    VALUES (%s, %s, %s, %s, %s, %s, ST_GeomFromText(%s, 4326), %s, %s)
    """
data = ("3", "1", "성북천 산책 코스", pd.to_datetime("2023-05-18"),
        "보문역에서 내려서 성북천을 따라가서 성북천 분수광장까지 가는 산책로입니다. 물소리를 들으면서 산책을 할 수도 있고, 오리와 물고기도 구경할 수 있어요.", "서울", location, "1", "1")
cursor.execute(query, data)
conn.commit()

