from settings import output_columns_ferry
from functions import get_id_from_bb
from pathlib import Path
import json
from datetime import datetime

def main(tt):
    data = dict.fromkeys(output_columns_ferry, [])

    print(data)

    transport_types = {'fly': ('fly', 'flight', 'plane'), 'bus': ('busferry', 'bus', 'nightbus'), 'train': ('train', 'nighttrain'), 
                                'ferry': ('ferry', 'carferry', 'trainferry')}
    
    transport_id = (1, 2, 3, 10)
    
    transport_types_id = {types: id for types, id in zip(transport_types, transport_id)}
    
    print(transport_types_id)
    print(transport_types['fly'])
    
    for t in tt:
        try:
            ttype = next(k for k, v in transport_types.items() if t in v)
            
            if ttype == 'fly':
                print(f'{t} in {ttype}: {transport_types_id[ttype]}')
            elif  ttype != 'fly':
                print(f'{t} in {ttype}: {transport_types_id[ttype]}')
        except:
            print(f'{t} is not in {list(transport_types.keys())}')
            continue
    
    print()
    
def in_coords(coords) -> str:
    return get_id_from_bb(coords)


def get_exchange_rates():
    target_file = Path('../files/currencies/exchange_rates_EUR.json')
    with open(target_file, mode='r') as f:
        currencies = json.load(f)
    up_to_date = currencies['meta']['last_updated_at']
    EUR_rates = currencies['data']
    
    currs = ('ARS', 'MAD', 'AED', 'UAH', 'LKR', 'SAR', 'HRK', 'RSD')
    
    up_to_date = datetime.strptime(up_to_date, '%Y-%m-%dT%H:%M:%SZ').date()
    
    print(f"last update of currency exchange rates for base EUR: {up_to_date}")
    for curr in currs:
        print(f"EUR 1 equals {curr} {EUR_rates[curr]['value']}")
    print(EUR_rates.keys(), len(EUR_rates.keys()), sep='\n')
    
    today = datetime.today().date()
   
    print(today, up_to_date, (today - up_to_date).days)
    
    #return up_to_date, EUR_rates


if __name__ == '__main__':
    # main()
    # main(['flight', 'nightbus', 'nighttrain', 'car', 'trainferry', 'rideshare', 'plane'])
    # print(in_coords((49.19347, 16.61441)))
    get_exchange_rates()