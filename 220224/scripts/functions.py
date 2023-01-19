from progress.bar import IncrementalBar
from settings import df_bb, df_airports, NOT_FOUND


# Progress bar class
class AweBar(IncrementalBar):
    suffix = '%(index)d/%(max)d - %(percent).1f%% - %(elapsed)d s - %(remaining_hours)d h'
    @property
    def remaining_hours(self):
        return self.eta // 3600

        
# seeks city id for given coordinates and bbox coordinates
def get_id_from_bb(coords: list) -> int:
    try:           
        cond_1 = (coords[0] >= df_bb['lat_1']) & (coords[0] <= df_bb['lat_2'])
        cond_2 = (coords[1] >= df_bb['lon_1']) & (coords[1] <= df_bb['lon_2'])
        
        filter_df = df_bb.filter(cond_1 & cond_2)
            
        return filter_df['id_city'][0]
        
    except:
        return NOT_FOUND    


# seeks city id for airport code like 'THR', 'AUB', 'GKO'
def get_id_from_acode(code: str) -> int:
    try:
        filter_df = df_airports.filter(df_airports['code'] == code.lower())
        
        return filter_df['id_city'][0]
    
    except:
        return NOT_FOUND  


def get_id_pair(fname: str) -> tuple:
    spec_cities = ('Tel-Aviv', 'Cluj-Napoca', 'Clermont-Ferrand', 'Chambery-Savoie', 'Ivano-Frankivsk', 'Winston-Salem',
                   'Yuzhno-Sakhalinsk', 'Petropavlovsk-Kamchatsky', 'Khanty-Mansiysk', 'Gorno-Altaysk', 'Ust-Kut', 
                   'Nikolaevsk-na-Amure', 'Ust-Maya', 'Ust-Nera', 'Ust-Kuyga', 'Naryan-Mar')
    fn = fname
    for city in spec_cities:
        fn = fn.replace(city, city.replace('-', ' '))
    id_pair = tuple(fn.split('-')[::2])
    city_pair = tuple(fn.split('-')[1::2])
    
    return id_pair, city_pair



""" def get_airport_id_for_loc(location: str):
    try:
        filter_df = df_loc_airports.filter(df_loc_airports['city'] in (location.split()))
            
        return filter_df['id'][0]
        
    except:
        return NOT_FOUND """

        
if __name__ == '__main__':
    
    #print(get_id_pair('10-Tel-Aviv-20-Clermont-Ferrand'))
    
    print(get_id_from_acode('fra'),  get_id_from_acode('hhn'))
    
    #print(get_bb_id([38.4511,68.9642]), type(get_bb_id([38.4511,68.9642]))) """
    
    