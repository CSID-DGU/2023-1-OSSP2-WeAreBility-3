import json


with open("jsonTest.json", encoding='UTF-8') as json_file:
    json_data = json.load(json_file)
    locations_data = json_data["data"]["locations"]
    points = ""
    for i in locations_data :
        points += str(i["latitude"]) + " "
        points += str(i["longitude"]) + " "
    
    print(points)