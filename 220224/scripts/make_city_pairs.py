import pandas as pd
import polars as pl
from pathlib import Path
from itertools import permutations


LOC_AIRPORTS_CSV = '../files/csv/locations with airports.csv'
CITY_PAIRS_CSV = '../files/csv/city_pairs.csv'

df = pd.read_csv(LOC_AIRPORTS_CSV, usecols=['id', 'city'])
df_perm = permutations(zip(df['id'], df['city']), 2)

city_pairs_list = []
for item in df_perm:
    city_pairs_list.append(next(df_perm))

df_city_pairs = pd.DataFrame(city_pairs_list)
print(df_city_pairs.head(), df_city_pairs.shape, sep='\n')    

df_city_pairs.to_csv(CITY_PAIRS_CSV, index=False, header=False)
