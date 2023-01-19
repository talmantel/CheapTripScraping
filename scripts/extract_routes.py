import pandas as pd
import time
import concurrent.futures
import logging
import DateTime
import compress_json
from pathlib import Path

from currency_converter import CurrencyConverter, SINGLE_DAY_ECB_URL

from settings import ALL_DIRECT_ROUTES_CSV, NO_ID_TRANSPORT_CSV, NOT_FOUND, LOGS_DIR, OUTPUT_JSON_DIR, min_output_columns
#from generators import gen_jsons
from functions import get_id_from_bb, get_id_from_acode, AweBar


logging.basicConfig(filename=LOGS_DIR/'extract_routes.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

 # create currency converter class instance
cc = CurrencyConverter(SINGLE_DAY_ECB_URL)

# set for store no id transport
no_id_transport_set = set()


def output_no_id_transport():
    print('Start no id transport output...')
    start_time = time.perf_counter()
    nidt_df = pd.DataFrame(no_id_transport_set)
    nidt_df.to_csv(NO_ID_TRANSPORT_CSV, index=False, header='no_id_transport')
    print(f'Output no id transport duration is: {time.perf_counter() - start_time}')
    
    
def add_header_to_output_csv():
    with open(ALL_DIRECT_ROUTES_CSV, mode='w') as f:
        f.writelines(",".join(min_output_columns) + '\n')
        

# outputs in csv and json files
def output_csv(response: pd.DataFrame):
    print('Start output to file...')
    start_time = time.perf_counter()
    response.to_csv(ALL_DIRECT_ROUTES_CSV, mode='a', index=False, header=False)
    print(f'Output results duration is: {time.perf_counter() - start_time}')
   
    
def data_extraction(pathes) -> None:
     
    # main data dictionary initialize
    data = {'from_city_id':[], 'from_city':[], 'to_city_id':[], 'to_city':[], 'path_id':[], 'path_name':[], 
            'from_node':[], 'to_node':[], 'from_id':[], 'to_id':[], 'transport':[], 'transport_id':[], 
            'from_airport':[], 'to_airport':[], 'price_min_EUR':[], 'price_max_EUR':[], 'price_local':[], 
            'currency_local':[], 'distance_km':[], 'duration_min':[]
    }     
        
    # transport set up
    transport_types = {'fly': ('fly', 'flight', 'plane'), 'bus': ('busferry', 'bus', 'nightbus'), 
                       'train': ('train', 'nighttrain', 'cartrain'), 'ferry': ('ferry', 'carferry')}
    transport_id = (1, 2, 3, 10)
    transport_types_id = {types: id for types, id in zip(transport_types, transport_id)}
    
    print('Start data extraction...')
    start_time = time.perf_counter()
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:
                            
            if route[0] in transport_types['fly']:
                """ data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city) """
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1])
                data['from_id'].append(get_id_from_acode(route[2][0]))
                data['to_id'].append(get_id_from_acode(route[3][0]))
                data['transport'].append(route[0])
                data['transport_id'].append(transport_types_id['fly'])
                data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0])
                                        
                if route[11][0][1] in cc.currencies:
                    data['price_min_EUR'].append(round(cc.convert(route[11][0][0], route[11][0][1])))
                    data['price_max_EUR'].append(round(cc.convert(route[11][2][0], route[11][2][1])))
                else:
                    data['price_min_EUR'].append(NOT_FOUND)  
                    data['price_max_EUR'].append(NOT_FOUND)
                            
                data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('')
                data['duration_min'].append(round(route[4] / 60)) # sec to min
                    
            # for other used types of vehicles            
            else:
                try:
                
                    ttype = next(k for k, v in transport_types.items() if route[1] in v)
                    
                    """ data['from_city_id'].append(from_city_id)
                    data['from_city'].append(from_city)
                    data['to_city_id'].append(to_city_id)
                    data['to_city'].append(to_city) """
                    data['path_id'].append(path_id)
                    data['path_name'].append(path[4])
                    data['from_node'].append(route[6][1])
                    data['to_node'].append(route[7][1])
                    data['from_id'].append(get_id_from_bb(route[6][2:4]))
                    data['to_id'].append(get_id_from_bb(route[7][2:4]))
                    data['transport'].append(route[1])
                    data['transport_id'].append(transport_types_id[ttype])
                    data['from_airport'].append('')
                    data['to_airport'].append('')
                            
                    if route[13][0][1] in cc.currencies:
                        data['price_min_EUR'].append(round(cc.convert(route[13][0][0], route[13][0][1])))
                        data['price_max_EUR'].append(round(cc.convert(route[13][2][0], route[13][2][1])))
                    else:
                        data['price_min_EUR'].append(NOT_FOUND)  
                        data['price_max_EUR'].append(NOT_FOUND)

                    data['price_local'].append(route[14][0][0])
                    data['currency_local'].append(route[14][0][1])
                    data['distance_km'].append(round(route[5]))
                    data['duration_min'].append(round(route[3] / 60)) # sec to min
                
                
                except StopIteration:
                    no_id_transport_set.add(route[1]) # for no id transport types
                    continue
                except:
                    logging.error(f'{DateTime.DateTime()} the exception occurred', exc_info=True)
                    continue
             
                              
    print(f'Extract data duration is: {time.perf_counter() - start_time}')
        
    data_analyze(data)


def data_analyze(response_data):
    
    print('Start data analyzing...')
    start_time = time.perf_counter()
    
    temp_df = pd.DataFrame(response_data, columns=min_output_columns, index=None)
    temp_df = temp_df.drop_duplicates()
        
    cond_1 = temp_df['from_id'] != temp_df['to_id']
    cond_2 = (temp_df['from_id'] != NOT_FOUND) & (temp_df['to_id'] != NOT_FOUND)
    cond_3 = temp_df['price_min_EUR'] > 0
    
    #df_filter = temp_df
    df_filter = temp_df[cond_1 & cond_2 & cond_3]        
    
    print(f'Data analyzing duration is: {time.perf_counter() - start_time}')
        
    output_csv(df_filter)
        
        
def gen_jsons():
    """_Summary_ 
                unzips files' content into json
        Generates:
                json: str
    """   
    # assign directory
    directory = OUTPUT_JSON_DIR
    # iterate over files in
    files = Path(directory).glob('*.json.gz')    
    for file in files:    
        data_extraction(compress_json.load(str(file)))
        

def main():
    
    print('Start process...')
    
    start_time = time.perf_counter()
    
    add_header_to_output_csv()
    
    gen_jsons()
    
    output_no_id_transport()
    
    print(f'Elapsed {(time.perf_counter() - start_time)/3600}h') 
    
    
if __name__ == '__main__':
    main()
