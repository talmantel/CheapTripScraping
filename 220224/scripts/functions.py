import requests
from bs4 import BeautifulSoup
import json


from progress.bar import IncrementalBar


from settings import df_bb, df_airports, NOT_FOUND


# Progress bar class
class AweBar(IncrementalBar):
    suffix = '%(index)d/%(max)d - %(percent).1f%% - %(elapsed)d s - %(remaining_hours)d h'
    @property
    def remaining_hours(self):
        return self.eta // 3600


# makes scrapping for given url
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
        
# scrap routine
def scrap_routes(from_city_country, to_city_country):
    
    base_url = 'https://www.rome2rio.com/map/'
    
    tmp_url = base_url + from_city_country + '/' + to_city_country
    
    return get_url(tmp_url)
        
        
# seeks city id for given coordinates and bbox coordinates
def get_bb_id(coords: list[float]) -> int:
    try:           
        cond_1 = (coords[0] >= df_bb['lat_1']) & (coords[0] <= df_bb['lat_2'])
        cond_2 = (coords[1] >= df_bb['lon_1']) & (coords[1] <= df_bb['lon_2'])
        
        filter_df = df_bb.filter(cond_1 & cond_2)
            
        return filter_df['id_city'][0]
        
    except:
        return NOT_FOUND    


# seeks city id for airport code like 'THR', 'AUB', 'GKO'
def get_airport_id(code: str) -> int:
    try:
        filter_df = df_airports.filter(df_airports['code'] == code.lower())
        
        return filter_df['id_city'][0]
    
    except:
        return NOT_FOUND  


""" def get_airport_id_for_loc(location: str):
    try:
        filter_df = df_loc_airports.filter(df_loc_airports['city'] in (location.split()))
            
        return filter_df['id'][0]
        
    except:
        return NOT_FOUND """
        
        
if __name__ == '__main__':
    print(get_airport_id('syd'),  type(get_airport_id('syd')))
    
    print(get_bb_id([38.4511,68.9642]), type(get_bb_id([38.4511,68.9642])))
    
    print(scrap_routes('Chicago, USA', 'Baku, Azerbaijan'))