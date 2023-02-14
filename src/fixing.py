import pandas as pd
import logging
from datetime import datetime
import sys
from pathlib import Path


from config import TRIPLES_CSV, ROUTES_TO_FIX_CSV, FIXED_IDS_CSV, VALID_ROUTES_CSV, LOGS_DIR, OUTPUT_CSV_DIR


logging.basicConfig(filename=LOGS_DIR/'fixing.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')


def fix_price(input_csv=ROUTES_TO_FIX_CSV):
    
    INPUT_CSV = input_csv
    
    try: 
        
        if not INPUT_CSV.is_file():
            raise FileNotFoundError(INPUT_CSV)
        
        print(f"Start fixing process from '{input_csv}' ...")
        
        df_input = pd.read_csv(INPUT_CSV, usecols=['from_id', 'to_id', 'transport_id', 'price_to_fix'])
        valid_trips = tuple(zip(df_input['from_id'], df_input['to_id'], df_input['transport_id'], df_input['price_to_fix']))
              
        if not FIXED_IDS_CSV.is_file():
            with open(FIXED_IDS_CSV, 'w') as val_file:
                val_file.write('path_id')
        df_fixed_ids = pd.read_csv(FIXED_IDS_CSV, index_col=None, dtype=int)
        
        if not VALID_ROUTES_CSV.is_file():
            df_triples = pd.read_csv(TRIPLES_CSV, index_col='path_id')
            df_triples.to_csv(VALID_ROUTES_CSV)
        df_output = pd.read_csv(VALID_ROUTES_CSV, index_col='path_id')
        
        for valid_trip in valid_trips:
            try:
                # in forward direction
                query = 'from_id == @valid_trip[0] and to_id == @valid_trip[1] and transport_id == @valid_trip[2]'
                if len(df_output.query(query)) == 0: raise Exception(valid_trip[:3])
                match_index = df_output.query(query).index
                if match_index not in list(df_fixed_ids['path_id']):
                    df_output.at[match_index, 'price_min_EUR'] = valid_trip[3]
                    df_fixed_ids.at[len(df_fixed_ids.index)] = match_index
                    print(type(match_index))
            
                # backward
                query = 'from_id == @valid_trip[1] and to_id == @valid_trip[0] and transport_id == @valid_trip[2]'
                if len(df_output.query(query)) == 0: raise Exception(valid_trip[:3])
                match_index = df_output.query(query).index
                if match_index not in list(df_fixed_ids['path_id']):
                    df_output.at[match_index, 'price_min_EUR'] = valid_trip[3]
                    df_fixed_ids.at[len(df_fixed_ids.index)] = match_index
            
            except Exception as err:
                logging.warning(f"In file: '{input_csv.name}' matches not found: "
                                f"from_id = {err.args[0][0]}, to_id = {err.args[0][1]}, transport_id = {err.args[0][2]}")
                continue

        df_output.to_csv(VALID_ROUTES_CSV)
        df_fixed_ids.to_csv(FIXED_IDS_CSV, index=None)
        
        print('Routes were fixed successfully!')
        
    except FileNotFoundError as err:
        print(f"File '{err}' with routes have to be fixed not found")
    
    except:
        logging.error(f"{datetime.today()} an exception occurred: ", exc_info=True)
            

if __name__ == '__main__':
    
    if sys.argv[1] == '-p':
        files = Path(OUTPUT_CSV_DIR).glob('routes_to_fix_*.csv')
        for file in files:
            fix_price(input_csv=file)
    else:
        fix_price()