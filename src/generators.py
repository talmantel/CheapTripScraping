from itertools import permutations
from pathlib import Path
import compress_json
import string

from config import df_bb, df_city_countries, OUTPUT_JSON_DIR
from functions import get_id_pair


def gen_city_country_pairs() -> tuple:
    
    city_countries_bboxes = set(df_city_countries['id_city']).intersection(df_bb['id_city'])
    
    for from_id_city, to_id_city in permutations(city_countries_bboxes, 2):
        
        # get the cities' and countries' names
        from_city_id, from_city, from_country = df_city_countries.filter(df_city_countries['id_city'] == from_id_city).row(0)
        to_city_id, to_city, to_country = df_city_countries.filter(df_city_countries['id_city'] == to_id_city).row(0)
    
        yield from_city_id, to_city_id, from_city, from_country, to_city, to_country
    
    
# unzips files' content into json and generates tuple   
def gen_jsons(source_dir) -> tuple:
    # iterate over files in
    files = Path(source_dir).glob('*.json.gz')
    for file in files:
        if source_dir == OUTPUT_JSON_DIR:
            from_id, to_id = get_id_pair(file.name, old_file_name=False)
        else: 
            from_id, to_id = get_id_pair(file.name)
        
        yield from_id, to_id, compress_json.load(str(file))
        
        
# generates short string triples like '-abc' in order to avoid missing city-country pair mystic effect
def gen_injection():
    letters = string.ascii_lowercase * 3
    for item in permutations(letters, 3):
        yield '-' + ''.join(item)


if __name__ == '__main__':
    
    """ with open('all_city_country_pairs_3.csv', 'w') as f:    
        for i, (from_city_id, from_city, from_country, to_city_id, to_city, to_country) in enumerate(gen_city_country_pairs()):
            f.writelines(f'{from_city_id},{to_city_id},{from_city},{from_country},{to_city},{to_country}\n') """
    
    pass

    """ x = gen_jsons()
    print(next(x)) """
    """ print(next(x))
    print(next(x)) """
    
