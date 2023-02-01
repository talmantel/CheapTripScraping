from generators import gen_jsons
from config import OUTPUT_CSV_DIR, EXCLUDED_CITIES
import json
from pathlib import Path

        
def get_hotels():
    
    booking = dict()
    source_dir = Path('../files/output/json_output/2_run_jsons_r2r')
    
    for _, to_id, pathes in gen_jsons(source_dir):
        if to_id not in EXCLUDED_CITIES:
            for path in pathes:
                for route in path[8]:
                    if route[0] == 'hotel': 
                        booking[to_id] = route[6][:-1]
                        print(to_id, route[6][:-1])
                        break
    
    with open(OUTPUT_CSV_DIR/'booking.json', mode='w') as file:
        json.dump(booking, file, sort_keys=True)
    
    print(booking.keys(), len(booking.keys()))
    
    
if __name__ == '__main__':
    get_hotels()