import json
import pymysql
from shapely import wkt
from shapely.geometry import MultiPoint, Point
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
userid=6
user_id=2
title=json_data["title"]
introduction=json_data["introduction"]

coordinates = [(37.58390867551281, 126.97538108722037), (37.58518352258369,126.9751032824032), 
               (37.58624220001076, 126.9751538787905), (37.58580507963764, 126.97450301310107), 
               (37.584976265454124, 126.97496748977429), (37.5839309066819, 126.97401116070839)
               ]
coordinates_start = [(37.5555, 126.8998)]
location_start = wkt.dumps(Point(coordinates_start))
location = wkt.dumps(MultiPoint(points))
print(location)

query = """
    INSERT INTO enrollment_courses (id, user_id, title, created_date, introduction, start_location_name, locations, distance, status, start_location) 
    VALUES (%s, %s, %s, %s, %s, %s, ST_GeomFromText(%s, 4326), %s, %s, ST_GeomFromText(%s, 4326))
    """
data = (str(userid), str(user_id), title, pd.to_datetime("2023-05-20"),
        introduction, "서울", location, "1", "1", location_start)
cursor.execute(query, data)
conn.commit()

query = """
    INSERT INTO course_tags (id, course_id, tag) 
    VALUES (%s, %s, %s)
    """
data = (str(userid), str(userid), tags)
cursor.execute(query, data)
conn.commit()

