import logging
from datetime import datetime
import csv
import json
import haversine as hs

from config import LOGS_DIR, OUTPUT_CSV_DIR, OUTPUT_JSON_DIR, INNER_JSON_DIR,\
                   OLD_OUTPUT_JSON_DIR, TRANSPORT_TYPES, TRANSPORT_TYPES_ID, OUTPUT_COLUMNS, RAW_CSV, df_cities
from functions import get_id_from_bb, get_id_from_acode, get_exchange_rates
from exchange import update_exchange_rates
from generators import gen_jsons
from filters import id_not_found, same_ids, mismatch_euro_zone_terms, currency_mismatch, bad_price_value, is_trans_nicolaescu
from treatment import treat_data


# logging config
logging.basicConfig(filename=LOGS_DIR/'extraction.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')


# set for store no id transport
no_id_transport = set()

# set for store unknown currencies
unknown_currencies = set()

# stores the unique routes from input pathes
unique_routes = set()

# counter for path_id
counter = {k: k * 10000 for k in df_cities['id_city']}
     
    
def extract_routine(input_data: tuple, euro_rates: dict) -> list():
    
    origin_id, destination_id, pathes = input_data
    
    # returning data list              
    raw_data = list()
    
    # extracts direct routes from all pathes
    for inner_path_id, path in enumerate(pathes):
        for route_id, route in enumerate(path[8][:-1]):
                                 
            if route[0] in TRANSPORT_TYPES['fly']: # for 'flight', 'plane', 'fly'
                
                from_id = get_id_from_acode(route[2][0]) # gets city's id from the airport code
                to_id = get_id_from_acode(route[3][0])
                
                if id_not_found(from_id, to_id): continue # at least one of ids was not found
                
                currency = route[11][0][1]
                if currency_mismatch(currency, euro_rates.keys()): # the currency is out of used currency list
                    unknown_currencies.add(currency)
                    continue 
                
                price_min = route[11][0][0]
                if bad_price_value(price_min): continue # the price has invalid value
                price_min_EUR = round(price_min / euro_rates[currency]['value']) # convertation to EUR
                
                duration_min = round(route[4] / 60) # sec to min 
                
                # checks mismatching to min price and duration terms for euro zone
                if mismatch_euro_zone_terms(from_id, to_id, price_min_EUR, duration_min): continue
                
                transport_id = TRANSPORT_TYPES_ID['fly']
                
                # to avoid duplicating routes
                num_of_unique_routes = len(unique_routes)
                unique_routes.add((from_id, to_id, transport_id, price_min_EUR))
                if num_of_unique_routes == len(unique_routes): continue
                         
                distance_km = round(hs.haversine(route[2][3:5], route[3][3:5])) # ffor flights only
                frequency_tpw = route[7]
                
                counter[from_id] += 1
                path_id = counter[from_id]
                
                # creates and writes to json file the parameters of certain route  
                inner_json = {'path_id': path_id,
                              'from_id': from_id,
                              'to_id': to_id,
                              'transport_id': transport_id,
                              'price_min_EUR': price_min_EUR,
                              'duration_min': duration_min,
                              'distance_km': distance_km,
                              'frequency_tpw': frequency_tpw,
                              'transporter': None
                            
                                }
                
                INNER_JSON_DIR.mkdir(parents=True, exist_ok=True)
                with open(f'{INNER_JSON_DIR}/{str(path_id)}.json', mode='w') as file:
                    json.dump(inner_json, file)
                
                # collects all avaliable data
                raw_data.append({'path_id': path_id,
                                 'origin_id': origin_id,
                                 'destination_id': destination_id,
                                 'inner_path_id': inner_path_id,
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
                    if id_not_found(from_id, to_id) or same_ids(from_id, to_id): continue
                    
                    transporter = route[10][8][0][0]                   
                    if is_trans_nicolaescu(ttype, transporter, from_id, to_id): continue                   
                                       
                    currency = route[13][0][1]
                    if currency_mismatch(currency, euro_rates): 
                        unknown_currencies.add(currency)
                        continue
                    
                    price_min = route[13][0][0]
                    if bad_price_value(price_min): continue
                    price_min_EUR = price_min / euro_rates[currency]['value']
                    if 0 < price_min_EUR < 1: price_min_EUR == 1
                    price_min_EUR = round(price_min_EUR)
                    
                    duration_min = round(route[3] / 60) # sec to min
                    
                    if mismatch_euro_zone_terms(from_id, to_id, price_min_EUR, duration_min): continue
                    
                    transport_id = TRANSPORT_TYPES_ID[ttype]
                    
                    # to avoid full duplicating routes
                    num_of_unique_routes = len(unique_routes)
                    unique_routes.add((from_id, to_id, transport_id, price_min_EUR))
                    if num_of_unique_routes == len(unique_routes): continue
                    
                    distance_km = round(route[5])
                    frequency_tpw = route[10][8][0][6]  
                    
                    counter[from_id] += 1
                    path_id = counter[from_id]
                    
                    # creates and writes the parameters of certain route to json file 
                    inner_json = {'path_id': path_id,
                                  'from_id': from_id,
                                  'to_id': to_id,
                                  'transport_id': transport_id,
                                  'price_min_EUR': price_min_EUR,
                                  'duration_min': duration_min,
                                  'distance_km': distance_km,
                                  'frequency_tpw': frequency_tpw,
                                  'transporter': transporter
                                    }
                
                    INNER_JSON_DIR.mkdir(parents=True, exist_ok=True)
                    with open(f'{INNER_JSON_DIR}/{str(path_id)}.json', mode='w') as file:
                        json.dump(inner_json, file)
                    
                    # collects all avaliable data
                    raw_data.append({'path_id': path_id,
                                     'origin_id': origin_id,
                                     'destination_id': destination_id,
                                     'inner_path_id': inner_path_id,
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
                    logging.error(f'{datetime.today()} an exception occurred', exc_info=True)
                    continue
                        
    return raw_data


def extract_data(source_dir=OUTPUT_JSON_DIR):
    
    print('\n Data extraction...', end='...')
    
    # get last currency exchange rates for EUR and update date
    ago_days, euro_rates = get_exchange_rates()
          
    # updates currency exchange rates    
    if ago_days > 1 and update_exchange_rates(): _, euro_rates = get_exchange_rates()
    
    #create output csv dir and add header to output csv file
    OUTPUT_CSV_DIR.mkdir(parents=True, exist_ok=True)
    with open(RAW_CSV, mode='w') as f:
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
    
    print('successfully!\n')
    
    
if __name__ == '__main__':
    #source_dir = OLD_OUTPUT_JSON_DIR
    #extract_data(OLD_OUTPUT_JSON_DIR)
    extract_data()
