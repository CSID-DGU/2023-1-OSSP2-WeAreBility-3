import json
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

with open('C:\\Hoin666\\2023-1-OSSP2-WeAreBility-3\\AI\\walking_path_similarity\\Course_6.json', encoding='UTF-8') as f:

    json_data = json.load(f)
    locations_data = json_data["pointDtos"]
    tag_data = json_data["courseTags"]
    tags=""
    for i in tag_data :
        tags += str(i["courseTagType"]) + " "
    points = []
    for i in locations_data :
        points.append((i["latitude"], i["longitude"]))
userid=7
user_id=2
course_id=7
title=json_data["title"]
introduction=json_data["introduction"]

coordinates = [(37.58390867551281, 126.97538108722037), (37.58518352258369,126.9751032824032), 
               (37.58624220001076, 126.9751538787905), (37.58580507963764, 126.97450301310107), 
               (37.584976265454124, 126.97496748977429), (37.5839309066819, 126.97401116070839)
               ]
coordinates_start = [(37.5555, 126.8998)]
location_start = wkt.dumps(MultiPoint(coordinates_start))
location = wkt.dumps(MultiPoint(points))
print(location)
print(location_start)
query = """
    INSERT INTO finish_courses (id, user_id, course_id, finish_date) 
    VALUES (%s, %s, %s, %s)
    """
data = (str(userid), str(user_id), str(course_id), pd.to_datetime("2023-05-20"))
cursor.execute(query, data)
conn.commit()

