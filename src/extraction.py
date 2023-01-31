import pandas as pd
import logging
from datetime import datetime
import compress_json
from pathlib import Path
import time


from config import NOT_FOUND, LOGS_DIR, OUTPUT_CSV_DIR, OUTPUT_JSON_DIR, output_columns, raw_csv
from generators import gen_jsons
from functions import get_id_from_bb, get_id_from_acode, get_exchange_rates, AweBar
from exchange import update_exchange_rates


logging.basicConfig(filename=LOGS_DIR/'extraction.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

# get currency exchange rates for EUR and update date
ago_days, euro_rates = get_exchange_rates()

# set for store no id transport
no_id_transport = set()

# set for store unknown currencies
un_currencies = set()
      

def add_header_to_output_csv():
    with open(OUTPUT_CSV_DIR/raw_csv, mode='w') as f:
        f.writelines(",".join(output_columns) + '\n')
        

# outputs in csv and json files
def output_csv(response: pd.DataFrame):
    response.to_csv(OUTPUT_CSV_DIR/raw_csv, mode='a', index=False, header=False)


def data_analyze(response_data):
    
    temp_df = pd.DataFrame(response_data, columns=output_columns, index=None)
    
    temp_df.drop_duplicates(inplace=True)
        
    cond_1 = temp_df['from_id'] != temp_df['to_id']
    cond_2 = (temp_df['from_id'] != NOT_FOUND) & (temp_df['to_id'] != NOT_FOUND)
    cond_3 = temp_df['price_min_EUR'] > 0
    
    #df_filter = temp_df
    df_filter = temp_df[cond_1 & cond_2 & cond_3]        
        
    output_csv(df_filter)
        
    
def data_extraction(pathes) -> None:
    
    # main data dictionary initialize        
    data = {'from_id':[], 'to_id':[], 'transport_id':[], 'price_min_EUR':[], 'duration_min':[]}       
    
    # transport set up
    transport_types = {'fly': ('fly', 'flight', 'plane'), 'bus': ('busferry', 'bus', 'nightbus'), 
                       'train': ('train', 'nighttrain', 'cartrain'), 'share': 'rideshare', 'ferry': ('ferry', 'carferry')}
    transport_id = (1, 2, 3, 8, 10)
    transport_types_id = {types: id for types, id in zip(transport_types, transport_id)}
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:
                            
            if route[0] in transport_types['fly']:
                """ data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1]) """
                data['from_id'].append(get_id_from_acode(route[2][0]))
                data['to_id'].append(get_id_from_acode(route[3][0]))
                #data['transport'].append(route[0])
                data['transport_id'].append(transport_types_id['fly'])
                """ data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0]) """
                                        
                if route[11][0][1] in euro_rates.keys():
                    data['price_min_EUR'].append(round(route[11][0][0] / euro_rates[route[11][0][1]]['value']))
                    #data['price_max_EUR'].append(round(cc.convert(route[11][2][0], route[11][2][1])))
                else:
                    data['price_min_EUR'].append(NOT_FOUND)  
                    #data['price_max_EUR'].append(NOT_FOUND)
                    un_currencies.add(route[11][0][1])
                            
                """ data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('') """
                data['duration_min'].append(round(route[4] / 60)) # sec to min
                    
            # for other used types of vehicles            
            else:
                try:
                
                    ttype = next(k for k, v in transport_types.items() if route[1] in v)
                    
                    """ data['from_city_id'].append(from_city_id)
                    data['from_city'].append(from_city)
                    data['to_city_id'].append(to_city_id)
                    data['to_city'].append(to_city)
                    data['path_id'].append(path_id)
                    data['path_name'].append(path[4])
                    data['from_node'].append(route[6][1])
                    data['to_node'].append(route[7][1]) """
                    data['from_id'].append(get_id_from_bb(route[6][2:4]))
                    data['to_id'].append(get_id_from_bb(route[7][2:4]))
                    # data['transport'].append(route[1])
                    data['transport_id'].append(transport_types_id[ttype])
                    """ data['from_airport'].append('')
                    data['to_airport'].append('') """
                            
                    if route[13][0][1] in euro_rates.keys():
                        data['price_min_EUR'].append(round(route[13][0][0] / euro_rates[route[13][0][1]]['value']))
                        # data['price_max_EUR'].append(round(cc.convert(route[13][2][0], route[13][2][1])))
                    else:
                        data['price_min_EUR'].append(NOT_FOUND)  
                        # data['price_max_EUR'].append(NOT_FOUND)
                        un_currencies.add(route[13][0][1])

                    """ data['price_local'].append(route[14][0][0])
                    data['currency_local'].append(route[14][0][1])
                    data['distance_km'].append(round(route[5])) """
                    data['duration_min'].append(round(route[3] / 60)) # sec to min
                
                
                except StopIteration:
                    no_id_transport.add(route[1]) # for no id transport types
                    continue
                except:
                    logging.error(f'{datetime.today()} the exception occurred', exc_info=True)
                    continue
             
    data_analyze(data)

        
def gen_jsons():
    
    add_header_to_output_csv()
    
    # iterate over the files in
    files = Path(OUTPUT_JSON_DIR).glob(f'*.json.gz')     
    for file in files:
        data_extraction(compress_json.load(str(file)))
        
    output_no_id_transport()


def output_no_id_transport():
    nidt_df = pd.DataFrame(no_id_transport)
    uncurr_df = pd.DataFrame(un_currencies)
    nidt_df.to_csv(OUTPUT_CSV_DIR/'no_id_transport.csv', index=False)
    uncurr_df.to_csv(OUTPUT_CSV_DIR/'un_currencies.csv', index=False)
    

def extract_data():
    OUTPUT_CSV_DIR.mkdir(parents=True, exist_ok=True)
    print('Start data extraction...')
    answer = input(f'Last currency exchange rates update: {ago_days} days ago.\
                   Update rates before extraction? (y/n) ')
    if answer in ('Y', 'y'): update_exchange_rates()     
    gen_jsons()
    print('Data extraction finished successfully!\n')
    
    
if __name__ == '__main__':
    
    extract_data()
