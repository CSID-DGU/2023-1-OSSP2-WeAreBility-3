import pymysql
import struct

# connect to your MySQL database using pymysql
conn = pymysql.connect(host="localhost", user="root", password="1234", db="naemansan")
cursor = conn.cursor()

# execute a query to retrieve the binary data as a string
cursor.execute("SELECT locations FROM courses")
binary_data = cursor.fetchone()
binary_data = bytes(list(binary_data)[0])
print(binary_data)

# unpack the binary data using the struct module
num_points = struct.unpack("10f", binary_data)
