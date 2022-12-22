import pandas as pd
import requests
from bs4 import BeautifulSoup
import json
import polars as pl
from currency_converter import CurrencyConverter
from pathlib import Path

# necessary pathes setting up
BBOXES_CSV = '../files/csv/bbox short.csv'
AIRPORTS_CSV = '../files/csv/airport codes short.csv'
LOC_AIRPORTS_CSV = '../files/csv/locations with airports.csv'
CITY_PAIRS_CSV = '../files/csv/city_pairs.csv'

# outputs 
ALL_DIRECT_ROUTES_CSV = '../files/output/csv_output/all_direct_routes.csv'
ALL_DIRECT_ROUTES_JSON = '../files/output/json_output/all_direct_routes.json'
NO_ID_TRANSPORT_CSV = '../files/output/csv_output/no_id_transport.csv'

NOT_FOUND = -1

# create currency converter class instances
cc = CurrencyConverter()

# default currency
DEFAULT_CUR = 'EUR'

# this function makes scrapping from the base_url
def get_url(url, english=True):
    try:
        if english:
            r = requests.get(url, headers={'Accept-Language': 'en-US,en;q=0.5'})
        else:
            r = requests.get(url)
        
        soup = BeautifulSoup(r.content, 'html.parser')
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
        parsed = json.loads(dis["content"])[2][1]

        #print(json.dumps(parsed, indent=4, sort_keys=True))
        return parsed    
            
        
    except:        
        print("Invalid page!")
        return []
        
""" if r.status_code != 200:
        print("Invalid page!")
        return []
    else:
        soup = BeautifulSoup(r.content, 'html.parser')
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
        parsed = json.loads(dis["content"])[2][1]

        #print(json.dumps(parsed, indent=4, sort_keys=True))
        return parsed """
    
    
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
            return NOT_FOUND    


# founds airport id for the airport`s code likes 'THR', 'AUB', 'GKO'
def get_airport_id(code: str) -> int:
    try:
        df = pl.read_csv(AIRPORTS_CSV, has_header=False, new_columns=['code', 'id'])
        filter_df = df.filter(df['code'] == code.lower())
        
        return filter_df['id'][0] 
    
    except:
        return NOT_FOUND  


def get_airport_id_for_loc(location: str) -> int:
    try:
        df = pl.read_csv(LOC_AIRPORTS_CSV, has_header=True)
        filter_df = df.filter(df['city'] == location)
            
        return filter_df['id'][0]
        
    except:
        return NOT_FOUND
        
    
# main data dictionary structure set up
data = {'from_city_id':[],
        'from_city':[],
        'to_city_id':[],
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
        'price_local':[], 
        'currency_local':[],
        'distance_km':[], 
        'duration_min':[]
}

# dict for store the rare transport type
no_id_transport = {
        'from_city_id':[],
        'from_city':[],
        'to_city_id':[],
        'to_city':[],
        'path_id':[],
        'path_name':[],
        'from_node':[],
        'to_node':[], 
        'from_id':[], 
        'to_id':[],
        'transport':[], 
        #'transport_id':[], 
        'from_airport':[], 
        'to_airport':[],
        'from_airport_id':[], 
        'to_airport_id':[],
        'price_EUR':[],
        'price_local':[], 
        'currency_local':[],
        'distance_km':[], 
        'duration_min':[]
} 

# transport codes manually set up
used_transport_types = ['fly', 'flight', 'bus', 'train', 'nighttrain', 'drive', 'car', 'taxi', 'walk', 'towncar', 
                        'rideshare', 'shuttle', 'carferry']
used_transport_id = {'fly': 1, 'flight': 1, 'bus': 2, 'train': 3, 'nighttrain': 3, 'drive': 4, 'car': 4, 'taxi': 5, 
                    'walk': 6, 'towncar': 7, 'rideshare': 8, 'shuttle': 9, 'carferry': 10}

# set up start and end points of path
#from_city, to_city = 'Madrid', 'Oslo'

#with open(CITY_PAIRS_CSV, 'r') as f:
city_pairs_df = pd.read_csv(CITY_PAIRS_CSV, header=None)
#print(city_pairs_df)
for city_pair in city_pairs_df.head(10).itertuples(index=False, name=None):
    from_city = city_pair[0].partition(',')[2][:-1]
    from_city_id = city_pair[0].partition(',')[0][1:]
    to_city = city_pair[1].partition(',')[2][:-1]
    to_city_id = city_pair[1].partition(',')[0][1:]
    #print(from_city_id, from_city, to_city_id, to_city)
    
# extract all avaliable pathes
    try:
        c = Cities()
        c.scrap_routes(from_city, to_city)
        pathes = c.routes[(from_city, to_city)]
        if pathes == []: raise Exception(f'Invalid page! {from_city}-{to_city}')
    except:
        continue

    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:
            
            # for fly and flights only                     
            if route[0] in (used_transport_types[:2]): 
                data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1])
                data['from_id'].append(get_bb_id(route[2][2:4]))
                data['to_id'].append(get_bb_id(route[3][2:4]))
                data['transport'].append(route[0])
                data['transport_id'].append(used_transport_id[route[0]])
                data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0])
                data['from_airport_id'].append(get_airport_id(route[2][0]))
                data['to_airport_id'].append(get_airport_id(route[3][0]))
                if route[11][0][1] not in (DEFAULT_CUR, ''):
                    try:
                        price_EUR = cc.convert(route[11][0][0], route[11][0][1], DEFAULT_CUR)
                    except:
                        continue
                    data['price_EUR'].append(round(price_EUR))
                else:
                    data['price_EUR'].append(route[11][0][0])
                #data['currency'].append(DEFAULT_CUR)
                data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('')
                data['duration_min'].append(int(route[4] / 60)) # sec to min
            
            # for used types of vehicles            
            elif route[1] in used_transport_types[2:]: 
                data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[6][1])
                data['to_node'].append(route[7][1])
                data['from_id'].append(get_bb_id(route[6][2:4]))
                data['to_id'].append(get_bb_id(route[7][2:4]))
                data['transport'].append(route[1])
                data['transport_id'].append(used_transport_id[route[1]])
                data['from_airport'].append('')
                data['to_airport'].append('')
                data['from_airport_id'].append(get_airport_id_for_loc(route[6][1]))
                data['to_airport_id'].append(get_airport_id_for_loc(route[7][1]))
                if route[13][0][1] not in (DEFAULT_CUR, ''):
                    try:
                        price_EUR = cc.convert(route[13][0][0], route[13][0][1], DEFAULT_CUR)
                    except:
                        continue
                    if price_EUR <= 1.0: price_EUR == 1.0
                    data['price_EUR'].append(round(price_EUR))
                else:
                    data['price_EUR'].append(route[13][0][0])
                #data['currency'].append(DEFAULT_CUR)           # may be unnecessary
                data['price_local'].append(route[14][0][0])
                data['currency_local'].append(route[14][0][1])
                data['distance_km'].append(round(route[5]))
                data['duration_min'].append(round(route[3] / 60)) # sec to min
            
            # for no id transport types
            else:
                no_id_transport['from_city_id'].append(from_city_id)
                no_id_transport['from_city'].append(from_city)
                no_id_transport['to_city_id'].append(to_city_id)
                no_id_transport['to_city'].append(to_city)
                no_id_transport['path_id'].append(path_id)
                no_id_transport['path_name'].append(path[4])
                no_id_transport['from_node'].append(route[6][1])
                no_id_transport['to_node'].append(route[7][1])
                no_id_transport['from_id'].append(get_bb_id(route[6][2:4]))
                no_id_transport['to_id'].append(get_bb_id(route[7][2:4]))
                no_id_transport['transport'].append(route[1])
                no_id_transport['from_airport'].append('')
                no_id_transport['to_airport'].append('')
                no_id_transport['from_airport_id'].append(get_airport_id_for_loc(route[6][1]))
                no_id_transport['to_airport_id'].append(get_airport_id_for_loc(route[7][1]))
                
                if route[13][0][1] not in (DEFAULT_CUR, ''):
                    try:
                        price_EUR = cc.convert(route[13][0][0], route[13][0][1], DEFAULT_CUR)
                    except:
                        continue
                    if price_EUR <= 1.0: price_EUR == 1.0
                    no_id_transport['price_EUR'].append(round(price_EUR))
                else:
                    no_id_transport['price_EUR'].append(route[13][0][0])
        
                no_id_transport['price_local'].append(route[14][0][0])
                no_id_transport['currency_local'].append(route[14][0][1])
                no_id_transport['distance_km'].append(round(route[5]))
                no_id_transport['duration_min'].append(round(route[3] / 60)) # sec to min
                
                                    
tmp_df = pd.DataFrame(data)
nidt_df = pd.DataFrame(no_id_transport)

# ouputs in csv and json files
tmp_df.to_csv(ALL_DIRECT_ROUTES_CSV, index=False)
tmp_df.to_json(ALL_DIRECT_ROUTES_JSON)

#output rare transport types
nidt_df.to_csv(NO_ID_TRANSPORT_CSV, index=False)
