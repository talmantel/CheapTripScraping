import pandas as pd
import time
#import multiprocessing
import concurrent.futures
import threading

from currency_converter import CurrencyConverter, SINGLE_DAY_ECB_URL


from settings import ALL_DIRECT_ROUTES_CSV, NO_ID_TRANSPORT_CSV, NOT_FOUND, output_columns, no_id_transport_set
from make_city_pairs import gen_city_country_pairs
from functions import get_bb_id, get_airport_id, scrap_routes, AweBar


def output_no_id_transport():
    print('Start no id transport output...')
    start_time = time.perf_counter()
    nidt_df = pd.DataFrame(no_id_transport_set)
    nidt_df.to_csv(NO_ID_TRANSPORT_CSV, index=False, header='no_id_transport')
    print(f'Output no id transport duration is: {time.perf_counter() - start_time}')
    
    
def add_header_to_output_res():
    with open(ALL_DIRECT_ROUTES_CSV, mode='w') as f:
        f.writelines(",".join(output_columns) + '\n')
        

# outputs in csv and json files
def output_res(response: pd.DataFrame):
    print('Start output to file...')
    start_time = time.perf_counter()
    response.to_csv(ALL_DIRECT_ROUTES_CSV, mode='a', index=False, header=False)
    print(f'Output results duration is: {time.perf_counter() - start_time}')
        
    #tmp_df.to_json(ALL_DIRECT_ROUTES_JSON, orient=)


# main loop for each city pair
def scrap_json(city_country):
    
    _, from_city, from_country, _, to_city, to_country = city_country
 
    # extract all avaliable pathes for each pair
    try:
        print('***************')
        print('Start json scraping...')
        start_time = time.perf_counter()
        pathes = scrap_routes(f'{from_city}, {from_country}', f'{to_city}, {to_country}')
        print(f'Json scraping duration is: {time.perf_counter() - start_time}')
        if pathes == []: raise Exception(f'Invalid page! {from_city}, {from_country} - {to_city}, {to_country}')
        return extract_data(city_country, pathes)
    except: 
        pass
    
def extract_data(city_country, pathes):
    
    from_city_id, from_city, _, to_city_id, to_city, _ = city_country
    
    # main data dictionary structure
    data = {
        'from_city_id':[], 'from_city':[], 'to_city_id':[], 'to_city':[], 'path_id':[], 'path_name':[], 
        'from_node':[], 'to_node':[], 'from_id':[], 'to_id':[], 'transport':[], 'transport_id':[], 
        'from_airport':[], 'to_airport':[], 'from_airport_id':[], 'to_airport_id':[], 'price_EUR':[], 
        'price_local':[], 'currency_local':[], 'distance_km':[], 'duration_min':[]
    }     
        
    # transport codes manually set up
    used_transport_types = ('fly', 'flight', 'plane', 'bus', 'busferry', 'nightbus', 'train', 'nighttrain', 
                            'rideshare', 'ferry', 'carferry')
    used_transport_id = {'fly': 1, 'flight': 1, 'plane': 1, 'bus': 2, 'busferry': 2, 'nightbus': 2, 'train': 3, 'nighttrain': 3, 
                        'rideshare': 8, 'ferry': 10, 'carferry': 10}

    # create currency converter class instances
    cc = CurrencyConverter(SINGLE_DAY_ECB_URL)
    
    print('Start data extraction...')
    start_time = time.perf_counter()
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route in path[8][:-1]:
                
            # for fly and flights only                     
            if route[0] in used_transport_types[:3]: 
                data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1])
                data['from_id'].append(get_airport_id(route[2][0]))
                data['to_id'].append(get_airport_id(route[3][0]))
                data['transport'].append(route[0])
                data['transport_id'].append(used_transport_id[route[0]])
                data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0])
                data['from_airport_id'].append(get_airport_id(route[2][0]))
                data['to_airport_id'].append(get_airport_id(route[3][0]))
                    
                if route[11][0][1] in cc.currencies:
                    data['price_EUR'].append(round(cc.convert(route[11][0][0], route[11][0][1])))
                else:
                    data['price_EUR'].append(NOT_FOUND)  
                        
                data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('')
                data['duration_min'].append(round(route[4] / 60)) # sec to min
                
            # for used types of vehicles            
            elif route[1] in used_transport_types[3:]: 
                data['from_city_id'].append(from_city_id)
                data['from_city'].append(from_city)
                data['to_city_id'].append(to_city_id)
                data['to_city'].append(to_city)
                data['path_id'].append(path_id)
                data['path_name'].append(path[4])
                data['from_node'].append(route[6][1])
                data['to_node'].append(route[7][1])
                data['from_id'].append(get_bb_id(route[6][2:4]))
                data['to_id'].append(get_bb_id(route[7][2:4]))
                data['transport'].append(route[1])
                data['transport_id'].append(used_transport_id[route[1]])
                data['from_airport'].append('')
                data['to_airport'].append('')
                data['from_airport_id'].append('')
                data['to_airport_id'].append('')
               # data['from_airport_id'].append(get_airport_id_for_loc(route[6][1]))
               # data['to_airport_id'].append(get_airport_id_for_loc(route[7][1]))
                    
                if route[13][0][1] in cc.currencies:
                    data['price_EUR'].append(round(cc.convert(route[13][0][0], route[13][0][1])))
                else:
                    data['price_EUR'].append(NOT_FOUND)

                data['price_local'].append(route[14][0][0])
                data['currency_local'].append(route[14][0][1])
                data['distance_km'].append(round(route[5]))
                data['duration_min'].append(round(route[3] / 60)) # sec to min
                
                # for no id transport types
            else:
                no_id_transport_set.add(route[1])
                
    print(f'Extract data duration is: {time.perf_counter() - start_time}')
    return data_analyze(data)


def data_analyze(data):
    
    print('Start data analyzing...')
    start_time = time.perf_counter()
    
    temp_df = pd.DataFrame(data, columns=output_columns, index=None)
        
    cond_1 = temp_df['from_id'] != temp_df['to_id']
    cond_2 = (temp_df['from_id'] != NOT_FOUND) & (temp_df['to_id'] != NOT_FOUND)
    cond_3 = (temp_df['price_EUR'] != NOT_FOUND) & (temp_df['price_EUR'] != 0)
        
    df_filter = temp_df[cond_1 & cond_2 & cond_3]    
    #if df_filter.empty: continue     
    
    print(f'Data analyzing duration is: {time.perf_counter() - start_time}')
        
    return output_res(df_filter)
        
    
if __name__ == '__main__':
    
    # progress bar
    #bar = AweBar('Processed', max = len(avaliable_id_pairs))
    city_pairs = []
    for i, item in enumerate(gen_city_country_pairs(), start=1):
        if i == 101: break
        city_pairs.append(item)
        
        
    
    print('Start process...')
    start_time = time.perf_counter()
    add_header_to_output_res()
    try:
        with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
            executor.map(scrap_json, city_pairs)
    except:
        print(f'Total duration is {time.perf_counter() - start_time}')
        
    """ for i, item in enumerate(gen_city_country_pairs(), start=1):
        if i == 101: break
        scrap_json(item) """
        
    output_no_id_transport()
    print(f'Processed {i - 1} city pairs, total duration is {time.perf_counter() - start_time}')
    #bar.finish()
