import json

#f = open('225-London-153-Cluj-Napoca.json', 'r')
f = open('293-124-Paris-Berlin.json', 'r')
data = json.load(f)

#Проверка пути на валидность
def check_path(data, path_list):
    if type(data) != list and type(data) != dict: return False
    #if type(path_list) != list: return False
    elem = data
    for i in path_list:
        try:
            elem = elem[i]
        except:
            continue
    return elem

y,x = 0,0
#спсиок всех мест где бывают цены
path_list = [["price_15",    [x,8,y,10,8,0,15]],
             ["price_16",    [x,8,y,10,8,0,16]],
             ["price_17_00", [x,8,y,10,8,0,17,0,0]],
             ["price_1701",  [x,8,y,10,8,0,17,0,1]],
             ["price_17_11", [x,8,y,10,8,0,17,1,1]],
             ["price_1801",  [x,8,y,10,8,0,18,0,1]],
             ["price_18_11", [x,8,y,10,8,0,18,1,1]],
             ["price_19_01", [x,8,y,19,0,1]],
             ["price_19_11", [x,8,y,19,1,1]],
             ["price_20_01", [x,8,y,20,0,1]],
             ["price_20_11", [x,8,y,20,1,1]],
             ["price_13",    [x,8,y,13]],
             ["price_14",    [x,8,y,14]]]


for x in range(0, len(data)):
    for y in range(0, 11):
        for path in path_list:
            price = check_path(data, path[1])
            if type(price) != list:
                pass
            elif len(price) == 0 or len(price[0]) < 4:
                pass
            else:
                path_price = path[0]
                currency = price[0][1]
                price = (price[0][0] + price[1][0] + price[2][0])/3
                print(x, y)
                print(path_price, currency, int(price))



#пути к ценам
#[0][1][]2
#price_15 = data[6][8][1][10][8][0][15]
#[0][1][]2
#price_16 = data[6][8][1][10][8][0][16]

#type of class
#price_17_00 = data[6][8][1][10][8][0][17][0][0]

#2nd_Class [0][1][2]
#price_1701 = data[6][8][1][10][8][0][17][0][1]

#type of class
#price_17_10 = data[6][8][1][10][8][0][17][1][0]

#1nd_Class [0][1][2]
#price_17_11 = data[6][8][1][10][8][0][17][1][1]

#type of class
#price_18_00 = data[6][8][1][10][8][0][18][0][0]

#2nd_Class [0][1][2]
#price_1801 = data[6][8][1][10][8][0][18][0][1]

#type of class
#price_18_10 = data[6][8][1][10][8][0][18][1][0]

#1nd_Class [0][1][2]
#price_18_11 = data[6][8][1][10][8][0][18][1][1]

#simple price
#price_13 = data[6][8][1][13]
#price_14 = data[6][8][1][14]

#type of class
#price_19_00 = data[6][8][1][19][0][0]

#2nd_Class [0][1][2]
#price_19_01 = data[6][8][1][19][0][1]

#type of class
#price_19_10 = data[6][8][1][19][1][0]

#2nd_Class [0][1][2]
#price_19_11 = data[6][8][1][19][1][1]

#type of class
#price_20_00 = data[6][8][1][20][0][0]

#2nd_Class [0][1][2]
#price_20_01 = data[6][8][1][20][0][1]

#type of class
#price_20_10 = data[6][8][1][20][1][0]

#2nd_Class [0][1][2]
#price_20_11 = data[6][8][1][20][1][1]