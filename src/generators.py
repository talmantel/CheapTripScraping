from itertools import permutations
from pathlib import Path
import compress_json
import string

from config import df_bb, df_city_countries, OUTPUT_JSON_DIR


def gen_city_country_pairs() -> tuple:
    
    city_countries_bboxes = set(df_city_countries['id_city']).intersection(df_bb['id_city'])
    
    for from_id_city, to_id_city in permutations(city_countries_bboxes, 2):
        
        # get the cities' and countries' names
        from_city_id, from_city, from_country = df_city_countries.filter(df_city_countries['id_city'] == from_id_city).row(0)
        to_city_id, to_city, to_country = df_city_countries.filter(df_city_countries['id_city'] == to_id_city).row(0)
    
        yield from_city_id, to_city_id, from_city, from_country, to_city, to_country
    
    
# unzips files' content into json    
def gen_jsons() -> str:
    # assign directory
    source_dir = OUTPUT_JSON_DIR
    # iterate over files in
    files = Path(source_dir).glob('*.json.gz') 
    for file in files:
        yield compress_json.load(str(file))
        
        
# generates short string triples like '-abc' in order to avoid missing city-country pair mystic effect
def gen_injection():
    letters = string.ascii_lowercase * 3
    for item in permutations(letters, 3):
        yield '-' + ''.join(item)


if __name__ == '__main__':
    
    """ x = gen_missing_pairs()
    print(next(x)) """
    
    """ with open('all_city_country_pairs_3.csv', 'w') as f:    
        for i, (from_city_id, from_city, from_country, to_city_id, to_city, to_country) in enumerate(gen_city_country_pairs()):
            f.writelines(f'{from_city_id},{to_city_id},{from_city},{from_country},{to_city},{to_country}\n') """
    """ xs = gen_city_country_pairs()
    for x in xs:
        print(x) """
    """ print(next(x))
    print(next(x)) """
    
    """ x = gen_injection()
    print(next(x))
    print(next(x))
    print(next(x)) """
    
