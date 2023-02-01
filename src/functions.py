from config import df_bb, df_airports, NOT_FOUND
from pathlib import Path
import json
from datetime import datetime, date
from progress.bar import IncrementalBar


        
# Progress bar class
class AweBar(IncrementalBar):
    suffix = '%(index)d/%(max)d - %(percent).1f%% - %(elapsed)d s - %(remaining_hours)d h'
    @property
    def remaining_hours(self):
        return self.eta // 3600
            
        
# seeks for city id by the given coordinates
def get_id_from_bb(coords: list) -> int:
    try:           
        cond_1 = (coords[0] >= df_bb['lat_1']) & (coords[0] <= df_bb['lat_2'])
        cond_2 = (coords[1] >= df_bb['lon_1']) & (coords[1] <= df_bb['lon_2'])
        
        filter_df = df_bb.filter(cond_1 & cond_2)
            
        return filter_df['id_city'][0]
        
    except:
        return NOT_FOUND    


# seeks for city id by airport code like 'THR', 'AUB', 'GKO' etc.
def get_id_from_acode(code: str) -> int:
    try:
        filter_df = df_airports.filter(df_airports['code'] == code.lower())
        
        return filter_df['id_city'][0]
    
    except:
        return NOT_FOUND  


def get_id_pair(fname: str, old_file_name: bool=True) -> tuple:
    spec_cities = ('Tel-Aviv', 'Cluj-Napoca', 'Clermont-Ferrand', 'Chambery-Savoie', 'Ivano-Frankivsk', 'Winston-Salem',
                   'Yuzhno-Sakhalinsk', 'Petropavlovsk-Kamchatsky', 'Khanty-Mansiysk', 'Gorno-Altaysk', 'Ust-Kut', 
                   'Nikolaevsk-na-Amure', 'Ust-Maya', 'Ust-Nera', 'Ust-Kuyga', 'Naryan-Mar')
    fn = fname
    for city in spec_cities:
        fn = fn.replace(city, city.replace('-', ' '))
    
    if old_file_name:
        from_id, to_id = fn.split('-')[::2]
        #from_city, to_city = fn.split('-')[1::2]
        return from_id, to_id #, from_city, to_city
    
    from_id, to_id = fn.split('-')[:2]
    #from_city, to_city = fn.split('-')[2:4]
    return from_id, to_id #, from_city, to_city


def get_exchange_rates() -> tuple:
    source_file = Path('../files/currencies/exchange_rates_EUR.json')
    try:
        with open(source_file, mode='r') as f:
            data = json.load(f)
            
        last_update_date = data['meta']['last_updated_at']
        ago_days = date.today() - datetime.strptime(last_update_date, '%Y-%m-%dT%H:%M:%SZ').date()
        
        exchange_rates = data['data']

        return ago_days.days, exchange_rates
            
    except FileNotFoundError:
        print(f'File not found: {source_file}')
          
        
    
if __name__ == '__main__':
    
    print(get_id_pair('10-Tel-Aviv-20-Clermont-Ferrand'))
    
    #print(get_id_from_acode('fra'),  get_id_from_acode('hhn'))
    
    #print(get_bb_id([38.4511,68.9642]), type(get_bb_id([38.4511,68.9642]))) """
    
    #print(get_exchange_rates())
    pass