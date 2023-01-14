import polars as pl
from pathlib import Path

DEFAULT_CUR = 'EUR'
NOT_FOUND = -1

# logs set up
LOGS_DIR = Path('logs/logs_all_direct_routes')
LOGS_FERRY_DIR = Path('logs/logs_ferry')

# set up necessary inputs
INPUT_CSV_DIR = Path('220224/files/csv')
AIRPORT_CODES_CSV = INPUT_CSV_DIR/'airport codes short.csv'
BBOXES_CSV = INPUT_CSV_DIR/'bbox_short.csv'
CITY_COUNTRY_CSV = INPUT_CSV_DIR/'Full_list_with_countries_CLEANED_for_scraping.csv'


# set up outputs 
OUTPUT_JSON_DIR = Path('220224/files/output/json_output')
OUTPUT_JSON_FERRY_DIR = OUTPUT_JSON_DIR/'ferry'
OUTPUT_CSV_DIR = Path('220224/files/output/csv_output')
ALL_DIRECT_ROUTES_CSV = OUTPUT_CSV_DIR/'all_direct_routes.csv'
FERRY_ROUTES_CSV = OUTPUT_CSV_DIR/'ferry_routes.csv'
NO_ID_TRANSPORT_CSV = OUTPUT_CSV_DIR/'no_id_transport.csv'
FERRY_ROUTES_CSV = OUTPUT_CSV_DIR/'ferry_routes.csv'

# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id_city', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
df_airports = pl.read_csv(AIRPORT_CODES_CSV, has_header=False, new_columns=['code', 'id_city'])
df_city_countries = pl.read_csv(CITY_COUNTRY_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])

# set up output columns
output_columns_ferry = ['from_city_id', 'from_city', 'to_city_id', 'to_city', 'path_id', 'path_name', 
                        'from_node', 'to_node', 'from_id', 'to_id', 'from_coords', 'to_coords',
                        'transport', 'transport_id', 'price_min_EUR', 'price_max_EUR', 'price_local', 'currency_local', 
                        'distance_km', 'duration_min']

min_output_columns = ['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min']

all_output_columns = ['from_city_id', 'from_city', 'to_city_id', 'to_city', 'path_id', 'path_name', 
        'from_node', 'to_node', 'from_id', 'to_id', 'transport', 'transport_id', 
        'from_airport', 'to_airport', 'price_min_EUR', 'price_max_EUR', 'price_local', 
        'currency_local', 'distance_km', 'duration_min']