
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
query = "SET foreign_key_checks = 0"
cursor.execute(query)
conn.commit()


coordinates = [(37.5535,126.9533), (37.5541, 126.9516), (37.5525,126.9512), (37.5526,126.9508),
                (37.5529,126.9507), (37.5528, 126.9501), (37.5523, 126.9503)]
coordinates_start = [(37.5555, 126.8998)]
location_start = wkt.dumps(MultiPoint(coordinates_start))
location = wkt.dumps(MultiPoint(coordinates))
print(location)
print(location_start)
query = """
    INSERT INTO courses (id, user_id, title, created_date, introduction, start_location_name, locations, distance, status) 
    VALUES (%s, %s, %s, %s, %s, %s, ST_GeomFromText(%s, 4326), %s, %s)
    """
data = ("5", "2", "쌍룡산 코스", pd.to_datetime("2023-05-18"),
        "인근 거주하는 사람들이 가벼우면서도 충분하게 산책할 수 있는 코스입니다", "서울", location, "1", "1")
cursor.execute(query, data)
conn.commit()

"""[충무로에서 명동]
37.5611 126.9932
37.5607 126.9834
37.5608 126.9818
37.5582 126.983
37.5579 126.9813
37.5575 126.9773
37.557  126.9773
37.5571 126.9764
설명 : 충무로역에서 내려서 명동역까지 걷는 산책로입니다. 주변에 맛집과 볼거리도 많고, 사람도 엄청 많아요.
태그 : 중구, 도심, 퇴근길, 핫플, 맛집, 카페, 문화

"""