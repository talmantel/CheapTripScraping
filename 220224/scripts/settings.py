import polars as pl
from pathlib import Path

DEFAULT_CUR = 'EUR'
NOT_FOUND = -1

# set up necessary inputs
AIRPORT_CODES_CSV = Path('../files/csv/airport codes short.csv')
CITY_COUNTRY_CSV = Path('../files/csv/Full_list_with_countries_CLEANED_for_scraping.csv')
BBOXES_CSV = Path('../files/csv/bbox short.csv')

# set up outputs 
ALL_DIRECT_ROUTES_CSV = Path('../files/output/csv_output/all_direct_routes.csv')
ALL_DIRECT_ROUTES_JSON = Path('../files/output/json_output/all_direct_routes.json')
NO_ID_TRANSPORT_CSV = Path('../files/output/csv_output/no_id_transport.csv')

# set up all dataframes
df_bb = pl.read_csv(BBOXES_CSV, has_header=False, new_columns=['id_city', 'lat_1', 'lat_2', 'lon_1', 'lon_2'])
df_airports = pl.read_csv(AIRPORT_CODES_CSV, has_header=False, new_columns=['code', 'id_city'])
df_city_countries = pl.read_csv(CITY_COUNTRY_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])

# set up output columns
output_columns = ['from_city_id', 'to_city_id', 'from_id', 'to_id', 'transport_id', 'price_min_EUR', 'price_max_EUR', 'duration_min']

# set for store no id transport
no_id_transport_set = set()