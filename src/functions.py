from config import NOT_FOUND, CURRENCY_JSON, CURRENCY_HRK, BBOXES_CSV, AIRPORT_CODES_CSV, CITIES_COUNTRIES_CSV
import json
from datetime import datetime, date
from progress.bar import IncrementalBar
import polars as pl
import re
from time import perf_counter


# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id_city', 'lat_min', 'lat_max', 'lon_min', 'lon_max'])
df_airports = pl.read_csv(AIRPORT_CODES_CSV, has_header=False, new_columns=['code', 'id_city'])
df_cities_countries = pl.read_csv(CITIES_COUNTRIES_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])


# Progress bar class
class AweBar(IncrementalBar):
    suffix = '%(index)d/%(max)d - %(percent).1f%% - %(elapsed)d s - %(remaining_hours)d h'
    @property
    def remaining_hours(self):
        return self.eta // 3600
                    
            
# attempt to catch the bus or train stations at the airports by station name       
def get_id_by_station_name(station_name: str) -> int:
    try:
        station_name = station_name.replace(',', ' ').split()
        id = list(filter(lambda x: len(x) != 0, 
                    map(lambda x: df_cities_countries.filter(pl.col('city') == x)['id_city'], station_name)))
        return id[0][0] 
    except:
        return NOT_FOUND


# attempt to catch the bus or train stations at the airports by airport code
def get_id_by_station_acode(station_name: str) -> int:
    try:
        station_name = station_name.replace(',', ' ').split()  
        id = list(filter(lambda x: x != NOT_FOUND, 
                    map(get_id_from_acode, 
                    filter(lambda x: re.fullmatch('[a-z|A-Z]{3}', x) != None, station_name))))
        return id[0]
    except:
        return NOT_FOUND
        
        
# seeks for city id by the given coordinates
def get_id_from_bb(coords: list) -> int:
    try:       
        in_lat = (df_bb['lat_min'] <= coords[0]) & (df_bb['lat_max'] >= coords[0])
        in_lon = (df_bb['lon_min'] <= coords[1]) & (df_bb['lon_max'] >= coords[1])
        
        id = df_bb.filter(in_lat & in_lon)['id_city']        
    
        return id[0]
        
    except:
        return NOT_FOUND    


# seeks for city id by airport code like 'THR', 'AUB', 'GKO' etc.
def get_id_from_acode(code: str) -> int:
    try:
        id = df_airports.filter(df_airports['code'] == code.lower())['id_city']
        
        return id[0]
    
    except:
        return NOT_FOUND  


def get_exchange_rates() -> tuple:
    
    try:
        with open(CURRENCY_JSON, mode='r') as f:
            currencies = json.load(f)
            
        with open(CURRENCY_HRK, mode='r') as f_2:
            hrk = json.load(f_2)
        
        last_update_date = currencies['meta']['last_updated_at']
        ago_days = date.today() - datetime.strptime(last_update_date, '%Y-%m-%dT%H:%M:%SZ').date()
        
        currencies['data']['HRK'] = hrk['data']['HRK']
        exchange_rates = currencies['data']

        return ago_days.days, exchange_rates
            
    except FileNotFoundError as err:
        print(f'File not found: {err.filename}')

    
def get_inner_json(pth, rt, route_dic):
    try:
        """ if route_dic[pth][8][rt][0] in ['walk', 'car', 'hotel']:
            return 'bad type of transport' """
        if route_dic[pth][8][rt][0] in ['flight', 'plane', 'fly']:
            return {"path_id": None,
                    "transport":   route_dic[pth][8][rt][0],
                    "air_0":       route_dic[pth][8][rt][2][0],
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
                    #"dur_path":    route_dic[pth][5],
                    "duration":   route_dic[pth][8][rt][4],
                    "price_min":    route_dic[pth][20][0][0],
                    "price_avg":    route_dic[pth][20][1][0],
                    "price_max":    route_dic[pth][20][2][0],
                    "currency":    route_dic[pth][20][0][1],
                    "frequency":   route_dic[pth][8][rt][8],
                    "num_transfers": len(route_dic[pth][8][rt][12]),
                    "transfers_info": route_dic[pth][8][rt][12]
                    }
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
                    "price_min":    route_dic[pth][20][0][0],
                    "price_avg":     route_dic[pth][20][1][0],
                    "price_max":    route_dic[pth][20][2][0],
                    #"cost_t_max":  route_dic[pth][8][rt][13][0][0],
                    #"cost_t_avg":   route_dic[pth][8][rt][13][1][0],
                    #"cost_tr_min": route_dic[pth][8][rt][13][2][0],
                    "currency":    route_dic[pth][20][0][1],
                    "dur_pth_max": route_dic[pth][5]
                    }
    except IndexError as err:
        return "Error:", err 
    
    
if __name__ == '__main__':
    
    #print(get_id_pair('10-Tel-Aviv-20-Clermont-Ferrand'))
    
    #print(get_id_from_acode('fra'),  get_id_from_acode('hhn'))
    
    #print(get_bb_id([38.4511,68.9642]), type(get_bb_id([38.4511,68.9642]))) """
    
    #print(get_exchange_rates())
    start = perf_counter()
    print(get_id_from_bb([45.55,5.90]))
    print(perf_counter() - start)
    pass