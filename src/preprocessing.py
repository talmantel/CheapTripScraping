import pandas as pd
from geopy.geocoders import Nominatim
from pathlib import Path


from config import CITIES_COUNTRIES_CSV, INPUT_CSV_DIR, AIRPORT_CODES_CSV, CITIES_CSV


def get_bboxes(city_country):
    geolocator = Nominatim(user_agent='terraqwerty')
    
    try:
        print(', '.join(city_country))        
        location = geolocator.geocode(', '.join(city_country))
                
        bbox = list(map(lambda x: round(float(x), 4), list(filter(lambda x: x[0] == 'boundingbox', location.raw.items()))[0][1]))
        lat_min, lat_max, lon_min, lon_max = bbox
        
        lat = round(float(list(filter(lambda x: x[0] == 'lat', location.raw.items()))[0][1]), 4)
        lon = round(float(list(filter(lambda x: x[0] == 'lon', location.raw.items()))[0][1]), 4)
        
        if (lat_min <= lat <= lat_max) and (lon_min <= lon <= lon_max):
            return bbox, [lat, lon] 
        
        return None
        
    except AttributeError as err:
        print(city_country, err)
  
             

def preprocessing():
    
    try:
        if not CITIES_COUNTRIES_CSV.is_file(): raise FileNotFoundError
        if not AIRPORT_CODES_CSV.is_file(): raise FileNotFoundError
    
        print('\nPreprocessing ...')
        
        df_cities_countries = pd.read_csv(CITIES_COUNTRIES_CSV, names=['id_city', 'city', 'country'], index_col='id_city')
    
        BBOXES_CSV = Path(INPUT_CSV_DIR/'bboxes.csv')
    
        if BBOXES_CSV.is_file():
            df_bboxes = pd.read_csv(BBOXES_CSV, names=['id_city', 'lat_min', 'lat_max', 'lon_min', 'lon_max'], index_col='id_city')
            unboxed_ids = set(df_cities_countries.index.values).difference(df_bboxes.index.values)
        else:
            df_bboxes = pd.DataFrame()
            unboxed_ids = df_cities_countries.index.values
            
        if CITIES_CSV.is_file():
            df_cities = pd.read_csv(CITIES_CSV, names=['id_city', 'city', 'lat', 'lon'], index_col='id_city')
        else:
            df_cities = pd.DataFrame()
        
        for id in unboxed_ids:
            try:
                city_country = df_cities_countries.loc[id, ['city', 'country']].values
                bbox, coords = get_bboxes(city_country)
                df_bboxes.loc[id, ['lat_min', 'lat_max', 'lon_min', 'lon_max']] = bbox
                df_cities.at[id, 'city'] = city_country[0]
                df_cities.loc[id, ['lat', 'lon']] = coords
           
            except TypeError as err:
                print(f'Cannot find bounding box and/or coordinates for: {id} {city_country}')
                continue
            
            except Exception as err:
                print(err)
                continue
                
        df_bboxes.sort_index(inplace=True)
        df_cities.sort_index(inplace=True)
        df_bboxes.to_csv(BBOXES_CSV, header=False)
        df_cities.to_csv(CITIES_CSV, header=False)
        
        print('successfully!')
        
    except FileNotFoundError as err:
        print(err)
        
    
        


if __name__ == '__main__':
    preprocessing()
    
    




