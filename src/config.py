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
CITIES_CSV = Path(INPUT_CSV_DIR/'cities.csv')
CITY_COUNTRY_CSV = Path(INPUT_CSV_DIR/'Full_list_with_countries_CLEANED_for_scraping.csv')
CURRENCY_JSON = Path('../files/currencies/exchange_rates_EUR.json')
CURRENCY_HRK = Path('../files/currencies/last_HRK_EUR_rates.json')

# set up outputs 
OUTPUT_JSON_DIR = Path('../output/json_output')
OUTPUT_CSV_DIR = Path('../output/csv_output')
OLD_OUTPUT_JSON_DIR = Path('../files/output/json_output/2_run_jsons_r2r')
HOTELS_DIR = Path('../files/hotels')

# target raw csv
raw_csv = 'all_direct_routes_raw.csv'

# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id_city', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
df_airports = pl.read_csv(AIRPORT_CODES_CSV, has_header=False, new_columns=['code', 'id_city'])
df_city_countries = pl.read_csv(CITY_COUNTRY_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])
df_cities = pl.read_csv(CITIES_CSV, has_header=False, new_columns=['id_city', 'city', 'lat', 'lon'])

NUMBER_OF_CITIES = df_cities.shape[0]

EXCLUDED_CITIES = ('19', '47', '185', '221', '361', 
                   '49', '110', '143', '144', '182', 
                   '188', '223', '238', '298', '313', 
                   '322', '328', '344', '355')

# set up output columns
output_columns = ('origin_id', 'destination_id', 'path_id', 'route_id', 'from_id', 'to_id', 
                  'transport_id', 'price_min_EUR', 'duration_min')

# these items can be extracted from the scrapped json
avaliable_data = ('from_city_id', 'from_city', 'to_city_id', 'to_city', 'path_id', 'path_name', 
                  'from_node', 'to_node', 'from_id', 'to_id', 'transport', 'transport_id', 
                  'from_airport', 'to_airport', 'price_min_EUR', 'price_max_EUR', 'price_local', 
                  'currency_local', 'distance_km', 'duration_min')

# cities with specific name
DASH_NAME_CITIES = ('Tel-Aviv', 'Cluj-Napoca', 'Clermont-Ferrand', 'Chambery-Savoie', 'Ivano-Frankivsk', 'Winston-Salem',
                    'Yuzhno-Sakhalinsk', 'Petropavlovsk-Kamchatsky', 'Khanty-Mansiysk', 'Gorno-Altaysk', 'Ust-Kut', 
                    'Nikolaevsk-na-Amure', 'Ust-Maya', 'Ust-Nera', 'Ust-Kuyga', 'Naryan-Mar')

# for Trans Nicolaescu case
ROMANIAN_CITIES = (338, 357, 134, 153, 268)
TRANS_NICOLAESCU = 'Trans Nicolaescu'