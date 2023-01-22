from itertools import permutations
from pathlib import Path
import compress_json

from settings import df_bb, df_airports, df_city_countries, OUTPUT_JSON_DIR, CITY_COUNTRY_CSV
from functions import get_id_pair


def gen_city_country_pairs() -> tuple:
   
    #union_bb_airports = set(df_bb['id_city']).union(df_airports['id_city'])
    #intersect_city_countries_bb_airports = set(df_city_countries['id_city']).intersection(union_bb_airports)
    
    intersect_city_countries_bb_airports = set(df_city_countries['id_city']).intersection(df_bb['id_city'])
    
    for from_id_city, to_id_city in permutations(intersect_city_countries_bb_airports, 2):
        
        # get the city name and country name
        from_city_id, from_city, from_country = df_city_countries.filter(df_city_countries['id_city'] == from_id_city).row(0)
        to_city_id, to_city, to_country = df_city_countries.filter(df_city_countries['id_city'] == to_id_city).row(0)
    
        yield from_city_id, from_city, from_country, to_city_id, to_city, to_country
    
    
def gen_jsons():
    """_Summary_ 
                unzips files' content into json
        Generates:
                json: str
    """   
    # assign directory
    directory = OUTPUT_JSON_DIR
    # iterate over files in
    files = Path(directory).glob('*.json.gz') 
    for file in files:
        from_id, to_id = file.name.split('-')[0], file.name.split('-')[2]
        yield compress_json.load(str(file))


def gen_missing_pairs() -> tuple:
    
    id_city_country = dict()
    with open(CITY_COUNTRY_CSV, 'r') as f:
        for line in f.readlines():
            line = tuple(line[:-1].split(','))
            id_city_country[line[0]] = line[1:]
    
    all_cities = set()
    with open('all_city_country_pairs_3.csv') as f:
        for line in f.readlines():
            all_cities.add(tuple(line.split(',')[:2]))
            
    # print(all_cities)
        
    processed_cities = set()
    files = Path(OUTPUT_JSON_DIR).glob('*.json.gz')
    for file in files:
        id_pair, _ = get_id_pair(file.name[:file.name.find('.')])
        processed_cities.add(id_pair)
    
    # print(processed_cities)
    
    missing_cities = all_cities.symmetric_difference(processed_cities)
    
    # print(len(missing_cities))

    for from_city_id, to_city_id in missing_cities:
        
        from_city, from_country = id_city_country[from_city_id][0], id_city_country[from_city_id][1]
        to_city, to_country = id_city_country[to_city_id][0], id_city_country[to_city_id][1]
        
        yield from_city_id, from_city, from_country, to_city_id, to_city, to_country
        


if __name__ == '__main__':
    
    """ x = gen_missing_pairs()
    print(next(x)) """
    
    """ with open('all_city_country_pairs_3.csv', 'w') as f:    
        for i, (from_city_id, from_city, from_country, to_city_id, to_city, to_country) in enumerate(gen_city_country_pairs()):
            f.writelines(f'{from_city_id},{to_city_id},{from_city},{from_country},{to_city},{to_country}\n') """

    
