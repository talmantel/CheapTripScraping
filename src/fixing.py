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
        
        df_input = pd.read_csv(input_csv, usecols=['from_id', 'to_id', 'transport_id', 'price_to_fix'], 
                               dtype={'from_id': 'Int32', 'to_id': 'Int32', 'transport_id': 'Int32', 'price_to_fix': 'Int32'})
              
        if not FIXED_IDS_CSV.is_file():
            with open(FIXED_IDS_CSV, 'w') as val_file:
                val_file.write('path_id')
        df_fixed_ids = pd.read_csv(FIXED_IDS_CSV, dtype={'path_id': 'Int32'})
        
        if not VALID_ROUTES_CSV.is_file():
            df_triples = pd.read_csv(TRIPLES_CSV, index_col=0, dtype={'from_id': 'Int32', 'to_id': 'Int32', 'transport_id': 'Int32', 
                                                                      'price_min_EUR': 'Int32', 'duration_min': 'Int32', 'num_transfers':'Int32'})
            df_triples.to_csv(VALID_ROUTES_CSV)
        df_output = pd.read_csv(VALID_ROUTES_CSV, index_col=0, dtype={'from_id': 'Int32', 'to_id': 'Int32', 'transport_id': 'Int32', 
                                                                      'price_min_EUR': 'Int32', 'duration_min': 'Int32', 'num_transfers':'Int32'})
        
        deleted, exist, no_exist, fixed = 0, 0, 0, 0
        for from_id, to_id, transport_id, price_to_fix in df_input.values:
            try:                  
                # queries in both direction
                forward_conds = 'from_id == @from_id and to_id == @to_id and transport_id == @transport_id'
                backward_conds = 'from_id == @to_id and to_id == @from_id and transport_id == @transport_id'
                
                # executes queries
                query_forward = df_output.query(forward_conds)
                query_backward = df_output.query(backward_conds)
                
                # raises exception if no found matches for forward direction
                if query_forward.empty:
                    no_exist += 1
                    if query_backward.empty: 
                        no_exist += 1
                        raise IndexError((from_id, to_id, transport_id))
                     
                    match_index_backward = query_backward.index[0]
                    
                    if match_index_backward in df_fixed_ids['path_id'].values:
                        exist += 1
                        continue
                    
                    # fixes price and adds index to fixed ids
                    df_output.at[match_index_backward, 'price_min_EUR'] = price_to_fix
                    df_fixed_ids.at[df_fixed_ids.shape[0] + 1, 'path_id'] = match_index_backward
                    fixed += 1
                      
                else:     
                    # gets index if match in forward direction is found
                    match_index_forward = query_forward.index[0]
                    
                    if match_index_forward in df_fixed_ids['path_id'].values:
                        exist += 1
                        continue
                    
                    # fixes price and adds index to fixed ids
                    df_output.at[match_index_forward, 'price_min_EUR'] = price_to_fix
                    df_fixed_ids.at[df_fixed_ids.shape[0] + 1, 'path_id'] = match_index_forward
                    fixed += 1
                
                    # checks if backward route empty
                    if query_backward.empty: 
                        no_exist += 1
                        raise IndexError((to_id, from_id, transport_id))
                    
                    match_index_backward = query_backward.index[0]
                    
                    if match_index_backward in df_fixed_ids['path_id'].values:
                        exist += 1
                        continue
                    
                    # fixes price and adds index to fixed ids
                    df_output.at[match_index_backward, 'price_min_EUR'] = price_to_fix
                    df_fixed_ids.at[df_fixed_ids.shape[0] + 1, 'path_id'] = match_index_backward
                    fixed += 1    
            
            
            except IndexError as err:
                logging.error(f"In file: '{input_csv.name}'\n\tmatches not found: "
                                f"from_id = {err.args[0][0]}, to_id = {err.args[0][1]}, transport_id = {err.args[0][2]}")
                continue
            
            except:
                logging.error(f"{datetime.today()} an exception occurred: ", exc_info=True)
        
        zero_price_index = df_output.query('price_min_EUR == 0').index
        df_output.drop(zero_price_index, inplace=True)
        deleted = len(zero_price_index)
        
        df_output.sort_index(inplace=True) 
        df_output.to_csv(VALID_ROUTES_CSV)
        
        df_fixed_ids.sort_values(by='path_id', inplace=True)
        df_fixed_ids.to_csv(FIXED_IDS_CSV, index=None)
                
        print(f'Report: {df_input.shape[0] * 2} routes were processed totally: ' 
                                    f'{no_exist} not found; '
                                    f'{fixed} fixed successfully (of which {deleted} with zero_price droped); '
                                    f'{exist * 2} have been fixed earlier;\n')
                
                
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
        fix_price(Path(ROUTES_TO_FIX_DIR)/sys.argv[2])