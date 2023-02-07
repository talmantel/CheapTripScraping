import pandas as pd
import logging
from datetime import datetime


from config import NOT_FOUND, LOGS_DIR, OUTPUT_CSV_DIR, OUTPUT_JSON_DIR, output_columns, raw_csv, ROMANIAN_CITIES, TRANS_NICOLAESCU
from functions import get_id_from_bb, get_id_from_acode, get_exchange_rates
from exchange import update_exchange_rates
from generators import gen_jsons


logging.basicConfig(filename=LOGS_DIR/'extraction.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

# set for store no id transport
no_id_transport = set()

# set for store unknown currencies
un_currencies = set()
        
    
def extract_routine(origin_id, destination_id, pathes, euro_rates) -> None:
    
    # main data dictionary initialize        
    data = {'origin_id':[], 'destination_id':[], 'path_id':[], 'route_id':[], 'from_id':[], 'to_id':[], 
            'transport_id':[], 'price_min_EUR':[], 'duration_min':[]}       
    
    # transport set up
    transport_types = {'fly': ('fly', 'flight', 'plane'), 
                       'bus': ('busferry', 'bus', 'nightbus'), 
                       'train': ('train', 'nighttrain', 'cartrain'), 
                       'share': 'rideshare', 
                       'ferry': ('ferry', 'carferry')}
    transport_id = (1, 2, 3, 8, 10)
    transport_types_id = {types: id for types, id in zip(transport_types, transport_id)}
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route_id, route in enumerate(path[8][:-1]):
                                 
            if route[0] in transport_types['fly']:
                
                from_id = get_id_from_acode(route[2][0])
                to_id = get_id_from_acode(route[3][0])
                price = route[11][0][0]
                currency = route[11][0][1]
                duration = round(route[4] / 60) # sec to min
                
                if from_id == NOT_FOUND or to_id == NOT_FOUND: continue
                
                if currency not in euro_rates.keys() or currency in (0, ""): 
                    un_currencies.add(currency)
                    continue   
                
                data['origin_id'].append(origin_id)
                data['destination_id'].append(destination_id)
                data['path_id'].append(path_id)
                data['route_id'].append(route_id)
                data['from_id'].append(from_id)
                data['to_id'].append(to_id)
                data['transport_id'].append(transport_types_id['fly'])
                data['price_min_EUR'].append(round(price / euro_rates[currency]['value']))
                data['duration_min'].append(duration)
                
                """ data['from_city'].append(from_city)
                data['to_city'].append(to_city)
                data['path_name'].append(path[4])
                data['from_node'].append(route[2][1])
                data['to_node'].append(route[3][1])
                data['transport'].append(route[0])
                data['from_airport'].append(route[2][0])
                data['to_airport'].append(route[3][0])
                data['price_max_EUR'].append(round(cc.convert(route[11][2][0], route[11][2][1])))   
                data['price_local'].append('')
                data['currency_local'].append('')
                data['distance_km'].append('') """
                                    
            # for other used types of vehicles            
            else:
                try:                  
                    ttype = next(k for k, v in transport_types.items() if route[1] in v)
                    
                    from_id = get_id_from_bb(route[6][2:4])
                    to_id = get_id_from_bb(route[7][2:4])
                    price = route[13][0][0]
                    currency = route[13][0][1]
                    duration = round(route[3] / 60)  # sec to min
                    transporter = route[10][8][0][0]
                    
                    if from_id == NOT_FOUND or to_id == NOT_FOUND: continue
                    
                    if from_id == to_id: continue               
                    
                    if currency not in euro_rates.keys() or currency in (0, ""): 
                        un_currencies.add(currency)
                        continue   
                    
                    if ttype == 'bus' and transporter == TRANS_NICOLAESCU:
                        if (from_id not in ROMANIAN_CITIES) or (to_id not in ROMANIAN_CITIES): continue
                    
                    data['origin_id'].append(origin_id)
                    data['destination_id'].append(destination_id)
                    data['path_id'].append(path_id)
                    data['route_id'].append(route_id)
                    data['from_id'].append(from_id)
                    data['to_id'].append(to_id)
                    data['transport_id'].append(transport_types_id[ttype])
                    data['price_min_EUR'].append(round(price / euro_rates[currency]['value']))
                    data['duration_min'].append(round(duration))
                   
                    """ data['from_city'].append(from_city)
                    data['to_city'].append(to_city)
                    data['path_name'].append(path[4])
                    data['from_node'].append(route[6][1])
                    data['to_node'].append(route[7][1])
                    data['transport'].append(route[1])
                    data['from_airport'].append('')
                    data['to_airport'].append('')       
                    data['price_max_EUR'].append(round(cc.convert(route[13][2][0], route[13][2][1])))
                    data['price_local'].append(route[14][0][0])
                    data['currency_local'].append(route[14][0][1])
                    data['distance_km'].append(round(route[5])) """
                    
                    
                except StopIteration:
                    no_id_transport.add(route[1]) # for no id transport types
                    continue
                except:
                    logging.error(f'{datetime.today()} the exception occurred', exc_info=True)
                    continue
             
    # data_analyze(data)
    temp_df = pd.DataFrame(data, columns=output_columns, index=None)
    temp_df.drop_duplicates(output_columns, inplace=True)
    
    # output to csv
    temp_df.to_csv(OUTPUT_CSV_DIR/raw_csv, mode='a', index=False, header=False)
    
    
def output_no_id_transport():
    nidt_df = pd.DataFrame(no_id_transport)
    uncurr_df = pd.DataFrame(un_currencies)
    nidt_df.to_csv(OUTPUT_CSV_DIR/'no_id_transport.csv', index=False)
    uncurr_df.to_csv(OUTPUT_CSV_DIR/'un_currencies.csv', index=False)


def extract_data(source_dir=OUTPUT_JSON_DIR):
    
    print('Start data extraction...')
    
    # get currency exchange rates for EUR and update date
    ago_days, euro_rates = get_exchange_rates()
    
    answer = input(f'Last currency exchange rates update: {ago_days} days ago. '
                   f'Update rates before extraction? (y/n) ')
    if answer in ('Y', 'y'): 
        update_exchange_rates()
        _, euro_rates = get_exchange_rates()     
    
    #add_header_to_output_csv()
    OUTPUT_CSV_DIR.mkdir(parents=True, exist_ok=True)
    with open(OUTPUT_CSV_DIR/raw_csv, mode='w') as f:
        f.writelines(",".join(output_columns) + '\n')
    
    #
    for from_id, to_id, pathes in gen_jsons(source_dir):
        extract_routine(from_id, to_id, pathes, euro_rates)
        
    #output_no_id_transport()
    nidt_df = pd.DataFrame(no_id_transport)
    uncurr_df = pd.DataFrame(un_currencies)
    nidt_df.to_csv(OUTPUT_CSV_DIR/'no_id_transport.csv', index=False)
    uncurr_df.to_csv(OUTPUT_CSV_DIR/'un_currencies.csv', index=False)
    
    print('Data extraction finished successfully!\n')
    
    
if __name__ == '__main__':
    #source_dir = Path('../files/output/json_output/2_run_jsons_r2r')
    extract_data()
