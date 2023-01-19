import pandas as pd
import requests
from bs4 import BeautifulSoup
import json
from pathlib import Path
from itertools import permutations
import time


import polars as pl
from progress.bar import IncrementalBar
from currency_converter import CurrencyConverter, SINGLE_DAY_ECB_URL

# Progress bar class
class AweBar(IncrementalBar):
    suffix = '%(index)d/%(max)d - %(percent).1f%% - %(elapsed)d s - %(remaining_hours)d h'
    @property
    def remaining_hours(self):
        return self.eta // 3600

# set up necessary inputs
BBOXES_CSV = Path('../files/csv/bbox short.csv')
AIRPORTS_CSV = Path('../files/csv/airport codes short.csv')
LOC_AIRPORTS_CSV = Path('../files/csv/locations with airports.csv')

# set up outputs 
ALL_DIRECT_ROUTES_CSV = Path('../files/output/csv_output/all_direct_routes.csv')
ALL_DIRECT_ROUTES_JSON = Path('../files/output/json_output/all_direct_routes.json')
NO_ID_TRANSPORT_CSV = Path('../files/output/csv_output/no_id_transport.csv')

# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
df_airports = pl.read_csv(AIRPORTS_CSV, has_header=False, new_columns=['code', 'city_id'])
df_loc_airports = pl.read_csv(LOC_AIRPORTS_CSV, columns=['id', 'city'])

# create currency converter class instances
cc = CurrencyConverter(SINGLE_DAY_ECB_URL)

# default settings
DEFAULT_CUR = 'EUR'
NOT_FOUND = -1

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
        #print("Invalid page!")
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
def get_bb_id(coords):
    try:           
        cond_1 = (coords[0] >= df_bb['lat_1']) & (coords[0] <= df_bb['lat_2'])
        cond_2 = (coords[1] >= df_bb['lon_1']) & (coords[1] <= df_bb['lon_2'])
        
        filter_df = df_bb.filter(cond_1 & cond_2)
            
        return filter_df['id'][0]
        
    except:
        return NOT_FOUND    


# founds airport id for the airport`s code likes 'THR', 'AUB', 'GKO'
def get_airport_id(code):
    try:
        filter_df = df_airports.filter(df_airports['code'] == code.lower())
        
        return filter_df['city_id'][0]
    
    except:
        return NOT_FOUND  


def get_airport_id_for_loc(location: str):
    try:
        filter_df = df_loc_airports.filter(df_loc_airports['city'] in (location.split()))
            
        return filter_df['id'][0]
        
    except:
        return NOT_FOUND

# main data dictionary initialize      
data = {
        'from_city_id':[], 'from_city':[], 'to_city_id':[], 'to_city':[], 'path_id':[], 'path_name':[], 
        'from_node':[], 'to_node':[], 'from_id':[], 'to_id':[], 'transport':[], 'transport_id':[], 
        'from_airport':[], 'to_airport':[], 'from_airport_id':[], 'to_airport_id':[], 'price_EUR':[], 
        'price_local':[], 'currency_local':[], 'distance_km':[], 'duration_min':[]
}

# set up output columns
output_columns = ['from_id', 'to_id', 'transport_id', 'price_EUR', 'duration_min']

# set for store no id transport
no_id_transport = set()

# transport codes manually set up
used_transport_types = ('fly', 'flight', 'bus', 'nightbus', 'train', 'nighttrain', 
                        'rideshare', 'ferry', 'carferry')
used_transport_id = {'fly': 1, 'flight': 1, 'bus': 2, 'nightbus': 2, 'train': 3, 'nighttrain': 3, 
                     'rideshare': 8, 'ferry': 10, 'carferry': 10}

# create city id pairs from 'airport codes short.csv'
id_pairs_from_airports = set()
for pair in permutations(df_airports['city_id'].unique(), 2):
    id_pairs_from_airports.add(pair)

# create city id pairs from 'locations with airports.csv'
id_pairs_from_loc_airports = set()
for pair in permutations(df_loc_airports['id'].unique(), 2):
    id_pairs_from_loc_airports.add(pair)

# intersection of two lists gives avaliable id pairs
avaliable_id_pairs = id_pairs_from_airports.intersection(id_pairs_from_loc_airports)

# dict for id city_name binding    
dict_id_city_name = dict(zip(df_loc_airports['id'], df_loc_airports['city']))

# progress bar
bar = AweBar('Processed', max = len(avaliable_id_pairs))


c = Cities()

# main loop for each city pair
for n, pair in enumerate(avaliable_id_pairs):
    from_city_id, to_city_id = pair[0], pair[1]
    from_city, to_city = dict_id_city_name[from_city_id], dict_id_city_name[to_city_id]
    
    #if n == 10: break
    
    bar.next()
    time.sleep(1)
    
    # extract all avaliable pathes for each pair
    try:
        c.scrap_routes(from_city, to_city)
        pathes = c.routes[(from_city, to_city)]
        if pathes == []: raise Exception(f'Invalid page! {from_city}-{to_city}')
    except:
        continue

    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:
                
            # for fly and flights only                     
            if route[0] in used_transport_types[:2]: 
                data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1])
                data['from_id'].append(get_airport_id(route[2][0]))
                data['to_id'].append(get_airport_id(route[3][0]))
                #data['from_id'].append(get_bb_id(route[2][2:4]))
                #data['to_id'].append(get_bb_id(route[3][2:4]))
                data['transport'].append(route[0])
                data['transport_id'].append(used_transport_id[route[0]])
                data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0])
                data['from_airport_id'].append(get_airport_id(route[2][0]))
                data['to_airport_id'].append(get_airport_id(route[3][0]))
                    
                if route[11][0][1] in cc.currencies:
                    data['price_EUR'].append(round(cc.convert(route[11][0][0], route[11][0][1])))
                else:
                    data['price_EUR'].append(NOT_FOUND)  
                        
                data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('')
                data['duration_min'].append(round(route[4] / 60)) # sec to min
                
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
                    
                if route[13][0][1] in cc.currencies:
                    data['price_EUR'].append(round(cc.convert(route[13][0][0], route[13][0][1])))
                else:
                    data['price_EUR'].append(NOT_FOUND)

                data['price_local'].append(route[14][0][0])
                data['currency_local'].append(route[14][0][1])
                data['distance_km'].append(round(route[5]))
                data['duration_min'].append(round(route[3] / 60)) # sec to min
                
                # for no id transport types
            else:
                no_id_transport.add(route[1])
    
    temp_df = pd.DataFrame(data, columns=output_columns)
    
    cond_1 = temp_df['from_id'] != temp_df['to_id']
    cond_2 = (temp_df['from_id'] != NOT_FOUND) & (temp_df['to_id'] != NOT_FOUND)
    cond_3 = temp_df['price_EUR'] != NOT_FOUND
    
    df_filter = temp_df[cond_1 & cond_2 & cond_3]
    df_filter.drop_duplicates(subset=['from_id', 'to_id', 'transport_id'], inplace=True)
            
    # ouputs in csv and json files
    if n==0:
        df_filter.to_csv(ALL_DIRECT_ROUTES_CSV, mode='w', index=False, columns=output_columns)
    else:
        df_filter.to_csv(ALL_DIRECT_ROUTES_CSV, mode='a', index=False, header=False, columns=output_columns)
        
        #tmp_df.to_json(ALL_DIRECT_ROUTES_JSON, orient=)

    data = {
        'from_city_id':[], 'from_city':[], 'to_city_id':[], 'to_city':[], 'path_id':[], 'path_name':[], 
        'from_node':[], 'to_node':[], 'from_id':[], 'to_id':[], 'transport':[], 'transport_id':[], 
        'from_airport':[], 'to_airport':[], 'from_airport_id':[], 'to_airport_id':[], 'price_EUR':[], 
        'price_local':[], 'currency_local':[], 'distance_km':[], 'duration_min':[]
    }    
        
#output rare transport types
nidt_df = pd.DataFrame(no_id_transport)
nidt_df.to_csv(NO_ID_TRANSPORT_CSV, index=False)

bar.finish()