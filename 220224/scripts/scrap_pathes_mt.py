import time
import concurrent.futures
import logging
import requests
from bs4 import BeautifulSoup
import json
import DateTime
import sys

import compress_json

from settings import OUTPUT_JSON_DIR, LOGS_DIR
from generators import gen_city_country_pairs, gen_missing_pairs


logging.basicConfig(filename=LOGS_DIR/'all_direct_routes.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

header = False
def write_missing_city_pairs(missed_pairs):
    
    from_city_id, from_city, from_country, to_city_id, to_city, to_country = missed_pairs
    
    if header == False:
        mode = 'w'
        header = True
    else: 
        mode = 'a'
    with open(f'missing_city_pairs.csv', mode) as f:    
        f.writelines(f'{from_city_id}, {from_city}, {from_country}, {to_city_id}, {to_city}, {to_country}\n')
        

# scrapping for each city pair
def scrap_routine(cities_countries_pairs):
    
    from_city_id, from_city, from_country, to_city_id, to_city, to_country = cities_countries_pairs
    
    base_url = 'https://www.rome2rio.com/map/'
    
    filepath = f'{OUTPUT_JSON_DIR}/{from_city_id}-{to_city_id}-{from_city}-{to_city}.json.gz'
    
    way = f'{from_city}-{from_country}/{to_city}-{to_country}'
    
    tmp_url = base_url + way
    
    # extract all avaliable pathes for each pair
    try:
        
        print('Start scrap routine ...')
        
        r = requests.get(tmp_url)
        
        soup = BeautifulSoup(r.content, 'html.parser')
        
        dis = soup.find('meta', id="deeplinkTrip")
              
        parsed = json.loads(dis["content"])[2][1]
        
        print(f'Start data recording in {from_city_id}-{from_city}-{to_city_id}-{to_city}.json.gz')
        
        compress_json.dump(parsed, filepath) 
                
    except TypeError:
        write_missing_city_pairs(cities_countries_pairs) 
        #scrap_routine((from_city_id, from_city, from_country, to_city_id, to_city, to_country + '-fgh'))
        #write_missing_city_pairs(cities_countries_pairs)
    except:
        logging.error(f" {DateTime.DateTime()} On {tmp_url} exception occurred", exc_info=True)
     
        
def start_threads(mode: str):
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        if mode == '-m':
            executor.map(scrap_routine, gen_missing_pairs())
        else:
            executor.map(scrap_routine, gen_city_country_pairs())


def main(arg='-o'):
    
    print(f'Start process at {DateTime.DateTime()}...')
    start_time = time.perf_counter()
    start_threads(arg)
    print(f'Elapsed {(time.perf_counter() - start_time)/60}h')

         
if __name__ == '__main__':
    main(sys.argv[1])
