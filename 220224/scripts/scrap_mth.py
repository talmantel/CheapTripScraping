import time
import concurrent.futures
import requests
from bs4 import BeautifulSoup
import json

import compress_json

from settings import OUTPUT_JSON_DIR
from make_city_pairs import gen_city_country_pairs



# extract all avaliable pathes for each pair
def scrap_json(cities_countries_pairs):
    
    from_city_id, from_city, from_country, to_city_id, to_city, to_country = cities_countries_pairs
     
    base_url = 'https://www.rome2rio.com/map/'
   
    way = f'{from_city}/{to_city}'
    
    tmp_url = base_url + way
    
    filepath = f'{OUTPUT_JSON_DIR}/{from_city_id}-{to_city_id}.json.gz'
    
    #headers={'Accept-Language': 'en-US,en;q=0.5'}
    
    try:
        
        r = requests.get(tmp_url)
        
        #if r.status_code != 200: raise Exception
        
        soup = BeautifulSoup(r.content, 'html.parser')
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
        parsed = json.loads(dis["content"])[2][1]      
    
        compress_json.dump(parsed, filepath)    
                
    except Exception as err:
        print(f'On the {tmp_url} happened:{type(err)=} {err.args}')
        
        
       
def start_threads():        
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        executor.map(scrap_json, gen_city_country_pairs())
    
    
if __name__ == '__main__':
    
    print('Start process...')
    
    start_time = time.perf_counter()
          
    start_threads()
    
    print(f'Elapsed {(time.perf_counter() - start_time)/60}h')
   
   

        
""" for i, item in enumerate(gen_city_country_pairs(), start=1):
        if i == 101: break
        scrap_json(item) """
        
    
    
    
