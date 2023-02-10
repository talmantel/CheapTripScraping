import logging
from datetime import datetime
import csv
import haversine as hs

from config import NOT_FOUND, LOGS_DIR, OUTPUT_CSV_DIR, OUTPUT_JSON_DIR,\
                   OLD_OUTPUT_JSON_DIR, ROMANIAN_CITIES, TRANS_NICOLAESCU,\
                   BAD_VALUES, TRANSPORT_TYPES, TRANSPORT_TYPES_ID, OUTPUT_COLUMNS, RAW_CSV
from functions import get_id_from_bb, get_id_from_acode, get_exchange_rates, mismatch_euro_zone_terms
from exchange import update_exchange_rates
from generators import gen_jsons


# logging config
logging.basicConfig(filename=LOGS_DIR/'extraction.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')

# set for store no id transport
no_id_transport = set()

# set for store unknown currencies
unknown_currencies = set()
        
    
def extract_routine(input_data: tuple, euro_rates: dict) -> dict:
    
    origin_id, destination_id, pathes = input_data
    
    valid_routes = set()
    
    # main data list              
    data = list()
    
    # extraction all direct routes from all pathes and filling the main data dictionary
    for path_id, path in enumerate(pathes):
        for route_id, route in enumerate(path[8][:-1]):
                                 
            if route[0] in TRANSPORT_TYPES['fly']:
                
                from_id = get_id_from_acode(route[2][0])
                to_id = get_id_from_acode(route[3][0])
                if from_id == NOT_FOUND or to_id == NOT_FOUND: continue
                
                currency = route[11][0][1]
                if currency not in euro_rates.keys() or currency in BAD_VALUES: 
                    unknown_currencies.add(currency)
                    continue
                
                price_min = route[11][0][0]
                if price_min in BAD_VALUES: continue
                price_min_EUR = round(price_min / euro_rates[currency]['value'])
                
                duration_min = round(route[4] / 60) # sec to min
                
                if mismatch_euro_zone_terms(from_id, to_id, price_min_EUR, duration_min): continue
                
                distance_km = round(hs.haversine(route[2][3:5], route[3][3:5]))
                frequency_tpw = route[7]
                transport_id = TRANSPORT_TYPES_ID['fly']
                
                if (from_id, to_id, transport_id, price_min_EUR, duration_min) in valid_routes: continue
                
                valid_routes.add((from_id, to_id, transport_id, price_min_EUR, duration_min))
                
                data.append({'origin_id': origin_id,
                             'destination_id': destination_id,
                             'path_id': path_id,
                             'route_id': route_id,
                             'from_id': from_id,
                             'to_id': to_id,
                             'transport_id': transport_id,
                             'price_min_EUR': price_min_EUR,
                             'duration_min': duration_min,
                             'distance_km': distance_km,
                             'frequency_tpw': frequency_tpw
                            })
                                    
            # for other used types of vehicles            
            else:
                try:                  
                    ttype = next(k for k, v in TRANSPORT_TYPES.items() if route[1] in v)
                    
                    from_id = get_id_from_bb(route[6][2:4])
                    to_id = get_id_from_bb(route[7][2:4])
                    if from_id == NOT_FOUND or to_id == NOT_FOUND: continue
                    if from_id == to_id: continue
                    
                    transporter = route[10][8][0][0]                   
                    if ttype == 'bus' and transporter == TRANS_NICOLAESCU:
                        if (from_id not in ROMANIAN_CITIES) or (to_id not in ROMANIAN_CITIES): continue                   
                                       
                    currency = route[13][0][1]
                    if currency not in euro_rates.keys() or currency in BAD_VALUES: 
                        unknown_currencies.add(currency)
                        continue
                    
                    price_min = route[13][0][0]
                    if price_min in BAD_VALUES: continue
                    price_min_EUR = price_min / euro_rates[currency]['value']
                    if 0 < price_min_EUR < 1: price_min_EUR == 1
                    price_min_EUR = round(price_min_EUR)
                    
                    duration_min = round(route[3] / 60) # sec to min
                    if mismatch_euro_zone_terms(from_id, to_id, price_min_EUR, duration_min): continue
                    
                    distance_km = round(route[5])
                    frequency_tpw = route[10][8][0][6]  
                    transport_id = TRANSPORT_TYPES_ID[ttype]
                    
                    if (from_id, to_id, transport_id, price_min_EUR, duration_min) in valid_routes: continue
                
                    valid_routes.add((from_id, to_id, transport_id, price_min_EUR, duration_min))
                    
                    data.append({'origin_id': origin_id,
                                 'destination_id': destination_id,
                                 'path_id': path_id,
                                 'route_id': route_id,
                                 'from_id': from_id,
                                 'to_id': to_id,
                                 'transport_id': transport_id,
                                 'price_min_EUR': price_min_EUR,
                                 'duration_min': duration_min,
                                 'distance_km': distance_km,
                                'frequency_tpw': frequency_tpw
                                })
                      
                except StopIteration:
                    no_id_transport.add(route[1]) # for no id transport types
                    continue
                except:
                    logging.error(f'{datetime.today()} the exception occurred', exc_info=True)
                    continue
                        
    return data


def extract_data(source_dir=OUTPUT_JSON_DIR):
    
    print('Start data extraction...')
    
    # get currency exchange rates for EUR and update date
    ago_days, euro_rates = get_exchange_rates()
    
    # get answer
    answer = input(f'Last currency exchange rates update: {ago_days} days ago.'
                   f' Update rates before extraction? (y/n) ')
    if answer in ('Y', 'y'): 
        update_exchange_rates()
        _, euro_rates = get_exchange_rates()     
    
    #create output csv dir and add header to output csv file
    OUTPUT_CSV_DIR.mkdir(parents=True, exist_ok=True)
    with open(OUTPUT_CSV_DIR/RAW_CSV, mode='w') as f:
        csv_writer = csv.DictWriter(f, fieldnames=OUTPUT_COLUMNS)
        csv_writer.writeheader()
    
        # main extraction loop: get data and output in csv
        for item in gen_jsons(source_dir):
            data = extract_routine(item, euro_rates)
            csv_writer.writerows(data)
        
    #output no id transport and unknown currencies
    with open(OUTPUT_CSV_DIR/'no_id_transport.csv', mode='w') as f:
        csv.writer(f).writerow(no_id_transport)
    with open(OUTPUT_CSV_DIR/'unknown_currencies.csv', mode='w') as f:
        csv.writer(f).writerow(unknown_currencies)
    
    print('Data extraction finished successfully!\n')
    
    
if __name__ == '__main__':
    #source_dir = OLD_OUTPUT_JSON_DIR
    extract_data(OLD_OUTPUT_JSON_DIR)
