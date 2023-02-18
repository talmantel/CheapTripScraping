from geopy.geocoders import Nominatim
import xlrd
import csv

"""
def excel_get_data : get city row(1) and country row(8) from excel file 

def get_bounding_box : connect to osm ,make validation ,get boundingbox and round coordinates 

"""


def excel_get_data(path: str) -> list:
    wb = xlrd.open_workbook(path)
    sheet = wb.sheet_by_index(0)
    sheet.cell_value(0, 0)
    global citylist
    citylist = []
    for i in range(sheet.nrows):
        city = sheet.cell_value(i, 1)
        country = sheet.cell_value(i, 8)
        citylist.append([city, country])
    print(citylist)
    return citylist


def get_bounding_box(citylist: list) -> dict:
    global boundingdata
    boundingdata = {}

    for city in citylist:
        geolocator = Nominatim(user_agent="Oleksandrpoliev@gmail.com")
        location = geolocator.geocode({'city': f"{city[0]}", 'country': f'{city[1]}'})
        if location is None:
            location = geolocator.geocode({'city': "Tehran", 'country': 'Iran'})
        for i, value in location.raw.items():
            if i == "boundingbox":
                value = [float(x) for x in value]
                value = [round(x, 4) for x in value]
                value = [str(x) for x in value]  # error in excel if dont convert
                if value in boundingdata.values():
                    value = [0, 0, 0, 0]
                    boundingdata[city[0]] = value
                    print(i, city, value)
                else:
                    boundingdata[city[0]] = value
                    print(i, city, value)
    return boundingdata


def write_to_file(boundingdata: dict):
    with open(r'C:\Users\admin\PycharmProjects\DATAJSON\data\data.csv', 'w') as f:
        for key, value in boundingdata.items():
            writer = csv.writer(f)
            data_split = key, value[0], value[1], value[2], value[3]
            writer.writerow(data_split)



if __name__ == '__main__':
    path = r"D:\data\Locations with airports.xlsx"
    excel_get_data(path)
    get_bounding_box(citylist)
    write_to_file(boundingdata)