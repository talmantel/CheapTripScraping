import pandas as pd
import polars as pl
from pathlib import Path
from itertools import permutations


LOC_AIRPORTS_CSV = '../files/csv/locations with airports.csv'
AIRPORTS_CSV = '../files/csv/airport codes short.csv'
CITY_PAIRS_CSV = '../files/csv/city_pairs.csv'


# create city id pairs from 'airport codes short.csv'
df_airports = pl.read_csv(AIRPORTS_CSV, has_header=False, new_columns=['code', 'city_id'])

id_pairs_from_airports = set()
for pair in permutations(df_airports['city_id'].unique(), 2):
    id_pairs_from_airports.add(pair)

# create city id pairs from 'locations with airports.csv'
df_loc_airports = pl.read_csv(LOC_AIRPORTS_CSV, columns=['id', 'city'])

dict_id_city_name = dict(zip(df_loc_airports['id'], df_loc_airports['city']))

id_pairs_from_loc_airports = set()
for pair in permutations(df_loc_airports['id'].unique(), 2):
    id_pairs_from_loc_airports.add(pair)

#intersection of two lists
avaliable_id_pairs = id_pairs_from_airports.intersection(id_pairs_from_loc_airports)

output_dict = {
                'from_id':[],
                'from_city':[],
                'to_id':[],
                'to_city':[]    
}
# loop for each city pair
for pair in avaliable_id_pairs:
    from_id, to_id = pair[0], pair[1]
    from_city, to_city = dict_id_city_name[from_id], dict_id_city_name[to_id]
    output_dict['from_id'].append(from_id)
    output_dict['from_city'].append(from_city)
    output_dict['to_id'].append(to_id)
    output_dict['to_city'].append(to_city)
    
    print(from_id, from_city, to_id, to_city)

df = pd.DataFrame(output_dict, columns=['from_id', 'from_city', 'to_id', 'to_city'],)
    
df.to_csv(CITY_PAIRS_CSV, mode='a', index=False)

print(len(avaliable_id_pairs))


    
