import pandas as pd
import time
import concurrent.futures
import logging
import requests
import json 
from bs4 import BeautifulSoup
import DateTime

import compress_json
from currency_converter import CurrencyConverter, SINGLE_DAY_ECB_URL


from settings import OUTPUT_JSON_DIR, NO_ID_TRANSPORT_CSV, NOT_FOUND, output_columns_ferry
from settings import OUTPUT_JSON_FERRY_DIR, LOGS_FERRY_DIR, FERRY_ROUTES_CSV
from generators import gen_city_country_pairs
from functions import get_bb_id, AweBar

logging.basicConfig(filename=LOGS_FERRY_DIR/'ferries.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

# set for store no id transport
no_id_transport_set = set()

def write_missing_city_pairs(missed_pairs):
    with open(f'missing_city_pairs.csv', 'a') as f:    
        f.writelines(f'{missed_pairs}\n')

def output_no_id_transport():
    nidt_df = pd.DataFrame(no_id_transport_set)
    nidt_df.to_csv(NO_ID_TRANSPORT_CSV, index=False, header='no_id_transport')
    
    
def add_header_to_output_csv():
    with open(FERRY_ROUTES_CSV, mode='w') as f:
        f.writelines(",".join(output_columns_ferry) + '\n')
        

# outputs in csv and json files
def output_csv(response: pd.DataFrame):
        response.to_csv(FERRY_ROUTES_CSV, mode='a', index=False, header=False)
        print(f'Data added at {time.time()}')
    
        
# output each city pair routes in separate json.gzip file    
def output_json_gzip(city_country, scraped_json):
    from_city_id, _, _, to_city_id, _, _ = city_country
    filepath = f'{OUTPUT_JSON_DIR}/{from_city_id}-{to_city_id}.json.gz'
    compress_json.dump(scraped_json, filepath) 


def output_json_ferry(city_country, data):
    from_city_id, _, _, to_city_id, _, _ = city_country
    filepath = f'{OUTPUT_JSON_FERRY_DIR}/{from_city_id}-{to_city_id}.json.gz'
    compress_json.dump(data, filepath)
    

# main loop for each city pair
def scrap_routine(cities_countries_pairs):
    
    _, from_city, from_country, _, to_city, to_country = cities_countries_pairs
    
    base_url = 'https://www.rome2rio.com/map/'
    
    way = f'{from_city}-{from_country}/{to_city}-{to_country}'
    
    tmp_url = base_url + way
    
    # extract all avaliable pathes for each pair
    try:
        
        print('Start scrap routine')
        
        r = requests.get(tmp_url, headers={'Accept-Language': 'en-US,en;q=0.5'})
        
        soup = BeautifulSoup(r.content, 'html.parser')
        
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
              
        parsed = json.loads(dis["content"])[2][1]
        
        data_extraction(cities_countries_pairs, parsed)    
                
    except Exception as err:
        logging.error(f" {DateTime.DateTime()} On {tmp_url} exception occurred", exc_info=True)
        if TypeError: write_missing_city_pairs(way)
        pass
        

def data_extraction(city_country, pathes):
    
    from_city_id, from_city, _, to_city_id, to_city, _ = city_country
    
    found = 0
    
    # main data dictionary initialize    
    data = {
            'from_city_id': [], 'from_city': [], 'to_city_id': [], 'to_city': [], 'path_id': [], 'path_name': [], 
            'from_node': [], 'to_node': [], 'from_id': [], 'to_id': [], 'from_coords': [], 'to_coords': [],
            'transport': [], 'transport_id': [], 'price_min_EUR': [], 'price_max_EUR': [], 'price_local': [], 
            'currency_local': [], 'distance_km': [], 'duration_min': []        
    }
    
    # transport set up
    transport_types = {'fly': ('fly', 'flight', 'plane'), 'bus': ('bus', 'nightbus'), 'train': ('train', 'nighttrain'), 
                        'share':'rideshare', 'ferry': ('busferry', 'ferry', 'carferry', 'trainferry')}
    
    transport_id = (1, 2, 3, 8, 10)
    
    transport_types_id = {types: id for types, id in zip(transport_types, transport_id)}

    # create currency converter class instances
    cc = CurrencyConverter(SINGLE_DAY_ECB_URL)
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:   
            
            try:
                ttype = next(k for k, v in transport_types.items() if route[1] in v)
                
                # for ferries only       
                if ttype == 'ferry': 
                    print(f'Start data extraction with {ttype}')
                    data['from_city_id'].append(from_city_id)
                    data['from_city'].append(from_city)
                    data['to_city_id'].append(to_city_id)
                    data['to_city'].append(to_city)
                    data['path_id'].append(path_id)
                    data['path_name'].append(path[4])
                    data['from_node'].append(route[6][1])
                    data['to_node'].append(route[7][1])
                    data['from_coords'].append(route[6][2:4])
                    data['to_coords'].append(route[7][2:4])
                    data['from_id'].append(get_bb_id(route[6][2:4]))
                    data['to_id'].append(get_bb_id(route[7][2:4]))
                    data['transport'].append(route[1])
                    data['transport_id'].append(transport_types_id[ttype])
                        
                    if route[13][0][1] in cc.currencies:
                        data['price_min_EUR'].append(round(cc.convert(route[13][0][0], route[13][0][1])))
                        data['price_max_EUR'].append(round(cc.convert(route[13][2][0], route[13][2][1])))
                    else:
                        data['price_min_EUR'].append(NOT_FOUND)  
                        data['price_max_EUR'].append(NOT_FOUND)

                    data['price_local'].append(route[14][0][0])
                    data['currency_local'].append(route[14][0][1])
                    data['distance_km'].append(round(route[5]))
                    data['duration_min'].append(round(route[3] / 60)) # sec to min
                    
                    found = 1               

                    print(data)
                    
            except Exception:
                no_id_transport_set.add(route[1]) # collect no id transport
                continue         
    
    if found == 1:
        print(data)
        data_analyze(data)


def data_analyze(response_data):
    
    print('Start data analyze')
       
    temp_df = pd.DataFrame(response_data, columns=output_columns_ferry)
        
    #cond_1 = temp_df['from_id'] != temp_df['to_id'] | temp_df['from_id'] != temp_df['to_id']
    #cond_2 = True #(temp_df['from_id'] != NOT_FOUND) & (temp_df['to_id'] != NOT_FOUND)
    #cond_3 = True #(temp_df['price_min_EUR'] > 0) & (temp_df['price_max_EUR'] > 0)
    
    #df_filter = temp_df[cond_1 & cond_2 & cond_3]        
        
    output_csv(temp_df)
        
        
def start_threads():        
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:
        executor.map(scrap_routine, gen_city_country_pairs())
        
    
if __name__ == '__main__':
    
    print('Start process...')
    
    start_time = time.perf_counter()
    
    add_header_to_output_csv()
        
    start_threads()
    
    output_no_id_transport()
    
    print(f'Elapsed {(time.perf_counter() - start_time)/3600}h')
   
        
    
    
    
