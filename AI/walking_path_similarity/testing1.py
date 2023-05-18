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



query = """
    INSERT INTO course_tags (id, course_id, tag) 
    VALUES (%s, %s, %s)
    """
data = ("3", "3", "성북구 개울가 스트레스해소 자연 힐링 러닝 강아지 퇴근길 고양이")
cursor.execute(query, data)
conn.commit()
