from config import df_bb, df_airports, NOT_FOUND, CURRENCY_JSON, CURRENCY_HRK, DASH_NAME_CITIES
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
    
    for city in DASH_NAME_CITIES:
        fname = fname.replace(city, city.replace('-', ' '))
    
    if old_file_name:
        from_id, to_id = fname.split('-')[::2]
        #from_city, to_city = fn.split('-')[1::2]
        return from_id, to_id #, from_city, to_city
    
    from_id, to_id = fname.split('-')[:2]
    #from_city, to_city = fn.split('-')[2:4]
    return from_id, to_id #, from_city, to_city


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
          
        
    
if __name__ == '__main__':
    
    #print(get_id_pair('10-Tel-Aviv-20-Clermont-Ferrand'))
    
    #print(get_id_from_acode('fra'),  get_id_from_acode('hhn'))
    
    #print(get_bb_id([38.4511,68.9642]), type(get_bb_id([38.4511,68.9642]))) """
    
    print(get_exchange_rates())
    pass