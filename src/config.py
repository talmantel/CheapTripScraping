import polars as pl
from pathlib import Path


BASE_URL = 'https://www.rome2rio.com/map/'

NOT_FOUND = -1

# logs set up
LOGS_DIR = Path('../logs')

# set up inputs
INPUT_CSV_DIR = Path('../files/csv')
AIRPORT_CODES_CSV = Path(INPUT_CSV_DIR/'airport_codes_short.csv')
BBOXES_CSV = Path(INPUT_CSV_DIR/'bbox_short.csv')
CITY_COUNTRY_CSV = Path(INPUT_CSV_DIR/'Full_list_with_countries_CLEANED_for_scraping.csv')

# set up outputs 
OUTPUT_JSON_DIR = Path('../output/json_output')
OUTPUT_CSV_DIR = Path('../output/csv_output')

# target raw csv
raw_csv = 'all_direct_routes_raw.csv'

# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id_city', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
df_airports = pl.read_csv(AIRPORT_CODES_CSV, has_header=False, new_columns=['code', 'id_city'])
df_city_countries = pl.read_csv(CITY_COUNTRY_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])

# set up output columns
output_columns = ['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min']

# these items can be extracted from the scrapped json
avaliable_data = ['from_city_id', 'from_city', 'to_city_id', 'to_city', 'path_id', 'path_name', 
                  'from_node', 'to_node', 'from_id', 'to_id', 'transport', 'transport_id', 
                  'from_airport', 'to_airport', 'price_min_EUR', 'price_max_EUR', 'price_local', 
                  'currency_local', 'distance_km', 'duration_min']