from itertools import permutations
from pathlib import Path
import compress_json
import string
import polars as pl

from config import OUTPUT_JSON_DIR, CITIES_COUNTRIES_CSV



def gen_city_country_pairs(input_csv=CITIES_COUNTRIES_CSV) -> tuple:
    
    try:
        input_csv = Path(input_csv)
        
        if not input_csv.is_file():
            raise FileNotFoundError(input_csv)

        # read input csv in DataFrame
        df = pl.read_csv(input_csv, has_header=False, new_columns=['id_city', 'city', 'country'])
        
        for from_id_city, to_id_city in permutations(df['id_city'], 2): # 
            
            # get the cities' and countries' names
            from_city_id, from_city, from_country = df.filter(df['id_city'] == from_id_city).row(0)
            to_city_id, to_city, to_country = df.filter(df['id_city'] == to_id_city).row(0)
        
            yield from_city_id, to_city_id, from_city, from_country, to_city, to_country
    
    except FileNotFoundError as err:
        print(f"Input file '{err.filename}' cannot be found")
    
    except Exception as err:
        print(err)
    
    
# unzips files' content into json and generates tuple   
def gen_jsons(source_dir=OUTPUT_JSON_DIR) -> tuple:
    # iterate over files in
    files = Path(source_dir).glob('*.json.gz')
    for file in files:
        
        from_id, to_id = file.name.split('-')[:2]
        
        yield from_id, to_id, compress_json.load(str(file))
        
        
# generates short string triples like '-abc' in order to avoid missing city-country pair mystic effect
def gen_injection():
    letters = string.ascii_lowercase * 3
    for item in permutations(letters, 3):
        yield '-' + ''.join(item)
        

if __name__ == '__main__':
    
    pass


    
