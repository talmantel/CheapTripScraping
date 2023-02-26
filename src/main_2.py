import json

# f = open('293-225-Paris-London.json', 'r')
# f = open('293-124-Paris-Berlin.json', 'r')
# f = open('35-329-Yerevan-Stockholm.json', 'r')

f = open('151-250-Cagliari-Mahon.json', 'r')
route_dic = json.load(f)
f.close()

def get_inner_json(pth, rt, route_dic):
    try:
        if route_dic[pth][8][rt][0] in ['walk', 'car', 'hotel']:
            return 'bad type of transport'
        elif route_dic[pth][8][rt][0] in ['flight']:
            return {"id": None,
                    "transport":   route_dic[pth][8][rt][0],
                    "Air_0":       route_dic[pth][8][rt][2][0],
                    "station_0":   route_dic[pth][8][rt][2][1],
                    "lat_0":       route_dic[pth][8][rt][2][3],
                    "lon_0":       route_dic[pth][8][rt][2][4],
                    "country_0":   route_dic[pth][8][rt][2][6],
                    "city_0":      route_dic[pth][8][rt][2][5],
                    "air_1":       route_dic[pth][8][rt][3][0],
                    "station_1":   route_dic[pth][8][rt][3][1],
                    "lat_1":       route_dic[pth][8][rt][3][3],
                    "lon_1":       route_dic[pth][8][rt][3][4],
                    "country_1":   route_dic[pth][8][rt][3][6],
                    "city_1":      route_dic[pth][8][rt][3][5],
                    "transporter": route_dic[pth][8][rt][2][5],
                    "dur_pth_max": route_dic[pth][5],
                    "dur_pth_avg": route_dic[pth][6],
                    "dur_pth_min": route_dic[pth][7],
                    "dur_rt_min":  route_dic[pth][8][rt][4],
                    "cost_min":    route_dic[pth][20][0][0],
                    "cost_HZ":     route_dic[pth][20][1][0],
                    "cost_max":    route_dic[pth][20][2][0],
                    "currency":    route_dic[pth][20][0][1],
                    "freq":        route_dic[pth][8][rt][8],
                    "plane_change": [len(route_dic[pth][8][rt][12]), route_dic[pth][8][rt][12]],
                    "time_wait":   route_dic[pth][8][rt][6]}          
        else:
            return {"id": None,
                    "transport":   route_dic[pth][8][rt][1],
                    "station_0":   route_dic[pth][8][rt][6][1],
                    "lat_0":       route_dic[pth][8][rt][6][2],
                    "lon_0":       route_dic[pth][8][rt][6][3],
                    "country_0":   route_dic[pth][8][rt][6][4],
                    "city_0":      route_dic[pth][8][rt][6][5],
                    "station_1":   route_dic[pth][8][rt][7][1],
                    "lat_1":       route_dic[pth][8][rt][7][2],
                    "lon_1":       route_dic[pth][8][rt][7][3],
                    "country_1":   route_dic[pth][8][rt][7][4],
                    "city_1":      route_dic[pth][8][rt][7][5],
                    "transporter": route_dic[pth][8][rt][10][8][0][0],
                    "www":         route_dic[pth][8][rt][10][8][0][2],
                    "phone":       route_dic[pth][8][rt][10][8][0][10],
                    "mail":        route_dic[pth][8][rt][10][8][0][11],
                    "cost_min":    route_dic[pth][20][0][0],
                    "cost_HZ":     route_dic[pth][20][1][0],
                    "cost_max":    route_dic[pth][20][2][0],
                    "cost_t_max":  route_dic[pth][8][rt][13][0][0],
                    "cost_t_HZ":   route_dic[pth][8][rt][13][1][0],
                    "cost_tr_min": route_dic[pth][8][rt][13][2][0],
                    "currency":    route_dic[pth][20][0][1],
                    "dur_pth_max": route_dic[pth][5],
                    "dur_pth_avg": route_dic[pth][6],
                    "dur_pth_min": route_dic[pth][7],
                    "dur_rt":      route_dic[pth][8][rt][3]}
    except IndexError as err:
        return "Error:", err


if __name__ == '__main__':
    
    for i in range(10):
        for j in range(10):
            q = get_inner_json(i, j, route_dic)
            print(q)
            print()
            print()
