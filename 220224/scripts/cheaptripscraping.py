import pandas as pd
import requests
from bs4 import BeautifulSoup
import json
import polars as pl
from currency_converter import CurrencyConverter
from pathlib import Path
# from itertools import permutations

# necessary pathes setting up
BBOXES_CSV = '220224/files/csv/bbox short.csv'
AIRPORTS_CSV = '220224/files/csv/airport codes short.csv'
LOC_AIRPORTS_CSV = '220224/files/csv/locations with airports.csv'
CITY_PAIRS_CSV = '220224/files/csv/city_pairs.csv'

ALL_CITIES_CSV = '220224/files/output/csv_output/all_cities.csv'

PATH_CSV_OUTPUT = '../files/output/csv_output/'
PATH_JSON_OUTPUT = '220224/files/output/json_output/'


NO_FOUND = -1

# create class instances for currency converter
cc = CurrencyConverter()

# default currency setting
DEFAULT_CUR = 'EUR'

# this function makes scrapping from the base_url
def get_url(url, english=True):
    if english:
        r = requests.get(url, headers={'Accept-Language': 'en-US,en;q=0.5'})
    else:
        r = requests.get(url)

    if r.status_code != 200:
        print("Invalid page!")
        return []
    else:
        data = {}
        soup = BeautifulSoup(r.content, 'html.parser')
        # data["title"] = soup.find('h1').text
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
        parsed = json.loads(dis["content"])[2][1]

        # print(json.dumps(parsed, indent=4, sort_keys=True))
        return parsed
    
    
class Cities:
    def __init__(self):
        self.base_url = 'https://www.rome2rio.com/map/'
        try:
            pass
        except FileNotFoundError:
            print("File Not Found")
        self.routes = {}

    def scrap_routes(self, city1, city2):
        tmp_url = self.base_url + city1 + '/' + city2
        self.routes[(city1, city2)] = get_url(tmp_url)
        
        
# founds bboxes from the coordinates
def get_bb_id(coords: float) -> int:
        try:
            df = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
            
            cond_1 = (coords[0] >= df['lat_1']) & (coords[0] <= df['lat_2'])
            cond_2 = (coords[1] >= df['lon_1']) & (coords[1] <= df['lon_2'])
        
            filter_df = df.filter(cond_1 & cond_2)
            
            return filter_df['id'][0]
        
        except:
            return NO_FOUND    


# founds airport id
def get_airport_id(code: str) -> int:
    try:
        df = pl.read_csv(AIRPORTS_CSV, has_header=False, new_columns=['code', 'id'])
        filter_df = df.filter(df['code'] == code.lower())
        
        return filter_df['id'][0] 
    
    except:
        return NO_FOUND  


def get_airport_id_for_loc(location: str) -> int:
    try:
        df = pl.read_csv(LOC_AIRPORTS_CSV, has_header=True)
        filter_df = df.filter(df['city'] == location)
            
        return filter_df['id'][0]
        
    except:
        return NO_FOUND
        
    
# main data dictionary structure set up
data = {'from_city':[],
        'to_city':[],
        'path_id':[],
        'path_name':[],
        'from_node':[],
        'to_node':[], 
        'from_id':[], 
        'to_id':[], 
        'transport':[],
        'transport_id':[], 
        'from_airport':[], 
        'to_airport':[],
        'from_airport_id':[], 
        'to_airport_id':[],
        'price_EUR':[],
        #'currency':[], # this key is may be unnecessary
        'price_local':[], 'currency_local':[],
        'distance_km':[], 
        'duration_min':[]}

# transport codes manually set up
transport_types = ['fly', 'flight', 'bus', 'train', 'nighttrain', 'drive', 'car', 'taxi', 'walk', 'towncar', 
                'rideshare', 'shuttle', 'carferry']
transport_id = {'fly': 1, 'flight': 1, 'bus': 2, 'train': 3, 'nighttrain': 3, 'drive': 4, 'car': 4, 'taxi': 5, 
                'walk': 6, 'towncar': 7, 'rideshare': 8, 'shuttle': 9, 'carferry': 10}

# set up start and end points of path
from_city, to_city = 'Madrid', 'Oslo'

#with open(CITY_PAIRS_CSV, 'r') as f:
#city_pairs_df = pd.read_csv(CITY_PAIRS_CSV, header=None)
#print(city_pairs_df)
#for city_pair in city_pairs_df.itertuples():
#(from_city, to_city) = city_pair
    
# extract all avaliable pathes
c = Cities()
c.scrap_routes(from_city, to_city)
pathes = c.routes[(from_city, to_city)]

# extraction all direct routes from all pathes and filling the main data dictionary
for path_id, path in enumerate(pathes):
    for route in path[8][:-1]:
                            
        if route[0] in (transport_types[:2]): # for fly and flights only
            data['from_city'].append(from_city)
            data['to_city'].append(to_city)
            data['path_id'].append(path_id)
            data['path_name'].append(path[4])
            data['from_node'].append(route[2][1])
            data['to_node'].append(route[3][1])
            data['from_id'].append(get_bb_id(route[2][2:4]))
            data['to_id'].append(get_bb_id(route[3][2:4]))
            data['transport'].append(route[0])
            data['transport_id'].append(transport_id[route[0]])
            data['from_airport'].append(route[2][0])
            data['to_airport'].append(route[3][0])
            data['from_airport_id'].append(get_airport_id(route[2][0]))
            data['to_airport_id'].append(get_airport_id(route[3][0]))
            if route[11][0][1] not in (DEFAULT_CUR, ''):
                price_EUR = cc.convert(route[11][0][0], route[11][0][1], DEFAULT_CUR)
                data['price_EUR'].append(round(price_EUR))
            else:
                data['price_EUR'].append(route[11][0][0])
            #data['currency'].append(DEFAULT_CUR)
            data['price_local'].append('')
            data['currency_local'].append('')
            data['distance_km'].append('')
            data['duration_min'].append(int(route[4] / 60)) # sec to min
                    
        elif route[1] in transport_types[2:]: # for other types of vehicles
            data['from_city'].append(from_city)
            data['to_city'].append(to_city)
            data['path_id'].append(path_id)
            data['path_name'].append(path[4])
            data['from_node'].append(route[6][1])
            data['to_node'].append(route[7][1])
            data['from_id'].append(get_bb_id(route[6][2:4]))
            data['to_id'].append(get_bb_id(route[7][2:4]))
            data['transport'].append(route[1])
            data['transport_id'].append(transport_id[route[1]])
            data['from_airport'].append('')
            data['to_airport'].append('')
            data['from_airport_id'].append(get_airport_id_for_loc(route[6][1]))
            data['to_airport_id'].append(get_airport_id_for_loc(route[7][1]))
            if route[13][0][1] not in (DEFAULT_CUR, ''):
                price_EUR = cc.convert(route[13][0][0], route[13][0][1], DEFAULT_CUR)
                if price_EUR <= 1.0: price_EUR == 1.0
                data['price_EUR'].append(round(price_EUR))
            else:
                data['price_EUR'].append(route[13][0][0])
            #data['currency'].append(DEFAULT_CUR)           # may be unnecessary
            data['price_local'].append(route[14][0][0])
            data['currency_local'].append(route[14][0][1])
            data['distance_km'].append(round(route[5]))
            data['duration_min'].append(round(route[3] / 60)) # sec to min
                                
tmp_df = pd.DataFrame(data)

        # set up outputs
        #csv_output_file = PATH_CSV_OUTPUT + f'{from_city}-{to_city}' + '.csv'
        #json_output_file = PATH_JSON_OUTPUT + f'{from_city}-{to_city}' + '.json'

tmp_df.to_csv(ALL_CITIES_CSV)
#tmp_df.to_json(json_output_file)
