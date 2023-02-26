import json

#f = open('225-London-153-Cluj-Napoca.json', 'r')
f = open('293-124-Paris-Berlin.json', 'r')
data = json.load(f)

def jsn_to_jsn(pth, rt):
    try:
        if data[pth][8][rt][0] in ['walk', 'car', 'flight', 'hotel']:
            print(data[pth][8][rt][0],'- is bad type')
        else:
            print(json.dumps({
            "id": None,
            "transport": data[pth][8][rt][1],
            "station_0": data[pth][8][rt][6][1],
            "lat_0": data[pth][8][rt][6][2],
            "lon_0": data[pth][8][rt][6][3],
            "country_0": data[pth][8][rt][6][4],
            "city_0": data[pth][8][rt][6][5],
            "station_1": data[pth][8][rt][7][1],
            "lat_1": data[pth][8][rt][7][2],
            "lon_1": data[pth][8][rt][7][3],
            "country_1": data[pth][8][rt][7][4],
            "city_1": data[pth][8][rt][7][5],
            "transporter": data[pth][8][rt][10][8][0][0],
            "www": data[pth][8][rt][10][8][0][2],
            "cost": data[pth][8][rt][10][8][0][15][0][0],
            "currency": data[pth][8][rt][10][8][0][15][0][1],
            }))
    except IndexError as err:
        print("Error:", err)

for x in range(0, 10):
    for y in range(0, 11):
        print(x,y)
        jsn_to_jsn(x, y)
