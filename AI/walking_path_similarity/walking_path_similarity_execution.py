import pymysql
from shapely import wkt
from shapely.geometry import MultiPoint
from shapely.wkb import loads
import struct


conn = pymysql.connect(
    host='localhost',
    user='root',
    password='1234',
    db='naemansan'
)

cursor = conn.cursor()


# Insert a new row into the courses table
coordinates = [(37.0, 127.0), (37.1, 127.1)]
location = wkt.dumps(MultiPoint(coordinates))
query = "INSERT INTO courses (title, start_location, locations) VALUES (%s, %s, ST_GeomFromText(%s, 4326))"
data = ("Hoin", "서울", location)
cursor.execute(query, data)
conn.commit()

# Retrieve all rows from the courses table and store the locations in a 2D array
query = "SELECT locations FROM courses"
cursor.execute(query)
results = cursor.fetchall()

locations_list = []

for row in results:
    for i in row:
        locations_list.append(struct.unpack('f',i))

print(locations_list)
