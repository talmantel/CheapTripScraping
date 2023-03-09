import pandas as pd
from geopy.geocoders import Nominatim
from pathlib import Path


from config import CITIES_COUNTRIES_CSV, INPUT_CSV_DIR, AIRPORT_CODES_CSV


def get_bboxes(city_country):
    geolocator = Nominatim(user_agent='Oleksandrpoliev@gmail.com')
    
    try:
                
        location = geolocator.geocode(', '.join(city_country))
                
        coords = map(float, list(filter(lambda x: x[0] == 'boundingbox', location.raw.items()))[0][1])
            
        lat_min, lat_max, lon_min, lon_max = map(lambda x: round(x, 4), coords)
            
        return lat_min, lat_max, lon_min, lon_max
        
    except AttributeError as err:
        print(city_country, err)
  
             

def preprocessing():
    
    try:
        if not CITIES_COUNTRIES_CSV.is_file(): raise FileNotFoundError
        if not AIRPORT_CODES_CSV.is_file(): raise FileNotFoundError
    
        print('\nPreprocessing ...', end='...')
        
        df_cities_countries = pd.read_csv(CITIES_COUNTRIES_CSV, names=['id_city', 'city','country'], index_col='id_city')
    
        BBOXES_CSV = Path(INPUT_CSV_DIR/'bbox_short.csv')
    
        if BBOXES_CSV.is_file():
            df_bboxes = pd.read_csv(BBOXES_CSV, names=['id_city', 'lat_min', 'lat_max', 'lon_min', 'lon_max'], index_col='id_city')
            unboxed_ids = set(df_cities_countries.index.values).difference(df_bboxes.index.values)
        else:
            df_bboxes = pd.DataFrame()
            unboxed_ids = df_cities_countries.index.values
        
        for id in unboxed_ids:
            city_country = df_cities_countries.loc[id, ['city', 'country']]
            df_bboxes.loc[id, ['lat_min', 'lat_max', 'lon_min', 'lon_max']] = get_bboxes(city_country)
        
        df_bboxes.sort_index(inplace=True)
        df_bboxes.to_csv(BBOXES_CSV, header=False)
        
        print('successfully!')
        
    except FileNotFoundError as err:
        print(err)
        
    except:
        pass


if __name__ == '__main__':
    preprocessing()
    
    




