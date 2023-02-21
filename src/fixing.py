import pandas as pd
import logging
from datetime import datetime
import sys
from pathlib import Path


from config import TRIPLES_CSV, FIXED_IDS_CSV, VALID_ROUTES_CSV, LOGS_DIR, ROUTES_TO_FIX_DIR


logging.basicConfig(filename=LOGS_DIR/'fixing.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')


def fix_price(input_csv):
    
    input_csv = Path(input_csv)
    
    try:   
        if not input_csv.is_file():
            raise FileNotFoundError(input_csv)
        
        print(f"Price fixing from '{input_csv}'...")
        
        df_input = pd.read_csv(input_csv, usecols=['from_id', 'to_id', 'transport_id', 'price_to_fix'])
        valid_trips = tuple(zip(df_input['from_id'], df_input['to_id'], df_input['transport_id'], df_input['price_to_fix']))
              
        if not FIXED_IDS_CSV.is_file():
            with open(FIXED_IDS_CSV, 'w') as val_file:
                val_file.write('path_id')
        df_fixed_ids = pd.read_csv(FIXED_IDS_CSV)
        
        if not VALID_ROUTES_CSV.is_file():
            df_triples = pd.read_csv(TRIPLES_CSV, index_col='path_id')
            df_triples.to_csv(VALID_ROUTES_CSV)
        df_output = pd.read_csv(VALID_ROUTES_CSV, index_col='path_id')
        
        for valid_trip in valid_trips:
            try:
                # in forward direction
                query_forward = 'from_id == @valid_trip[0] and to_id == @valid_trip[1] and transport_id == @valid_trip[2]'
                
                if df_output.query(query_forward).shape[0] == 0: raise IndexError(valid_trip[:3])
                match_index = df_output.query(query_forward).index[0]
                
                # drops record if price_to_fix == 0
                if valid_trip[3] == 0: 
                    df_output.drop(index=match_index, axis=0, inplace=True)
                    continue
                
                if match_index not in list(df_fixed_ids['path_id']):
                    df_output.at[match_index, 'price_min_EUR'] = valid_trip[3]
                    df_fixed_ids.at[df_fixed_ids.shape[0]] = match_index
                    print(match_index)
            
                # backward
                query_backward = 'from_id == @valid_trip[1] and to_id == @valid_trip[0] and transport_id == @valid_trip[2]'
             
                if df_output.query(query_backward).shape[0] == 0: raise IndexError(valid_trip[:3])
                match_index = df_output.query(query_backward).index[0]
                
                if match_index not in list(df_fixed_ids['path_id']):
                    df_output.at[match_index, 'price_min_EUR'] = valid_trip[3]
                    df_fixed_ids.at[df_fixed_ids.shape[0]] = match_index
                    print(match_index)
            
            except IndexError as err:
                logging.error(f"In file: '{input_csv.name}'\n\tmatches not found: "
                                f"from_id = {err.args[0][0]}, to_id = {err.args[0][1]}, transport_id = {err.args[0][2]}")
                continue
            
            
        df_output.to_csv(VALID_ROUTES_CSV)
        df_fixed_ids.to_csv(FIXED_IDS_CSV, index=None)
        
        print('......successfully!\n')
        
    except FileNotFoundError as err:
        print(f"File '{err}' with routes have to be fixed not found")
    
    except:
        logging.error(f"{datetime.today()} an exception occurred: ", exc_info=True)
            

if __name__ == '__main__':
    
    if len(sys.argv) > 1 and sys.argv[1] == '-b':
        files = Path(ROUTES_TO_FIX_DIR).glob('*.csv')
        for file in files:
            fix_price(file)
            
    elif len(sys.argv) > 2 and sys.argv[1] == '-f':
        print(sys.argv[2])
        fix_price(Path(ROUTES_TO_FIX_DIR)/sys.argv[2])