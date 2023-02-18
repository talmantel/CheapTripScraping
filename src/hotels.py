import json


from config import EXCLUDED_CITIES, HOTELS_DIR, OLD_OUTPUT_JSON_DIR, NUMBER_OF_CITIES
from generators import gen_jsons
        
        
def get_hotels():
    
    booking = dict()
    
    for _, to_id, pathes in gen_jsons(OLD_OUTPUT_JSON_DIR):
        
        if (to_id in EXCLUDED_CITIES) or (to_id in booking.keys()): continue
        
        booking[to_id] = pathes[0][8][-1][6][:-1]
        
        print(to_id, booking[to_id])
        
        if len(booking.keys()) == NUMBER_OF_CITIES: break
    
    HOTELS_DIR.mkdir(parents=True, exist_ok=True)
    with open(HOTELS_DIR/'booking.json', mode='w') as file:
        json.dump(booking, file, sort_keys=True)
    
    print(booking.keys(), len(booking.keys()))
    
    
if __name__ == '__main__':
    get_hotels()