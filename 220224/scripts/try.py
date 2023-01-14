from settings import output_columns_ferry
from functions import get_id_from_bb

def main(tt):
    data = dict.fromkeys(output_columns_ferry, [])

    print(data)

    transport_types = {'fly': ('fly', 'flight', 'plane'), 'bus': ('bus', 'nightbus'), 'train': ('train', 'nighttrain'), 
                                'share':'rideshare', 'ferry': ('busferry', 'ferry', 'carferry', 'trainferry')}
    
    transport_id = (1, 2, 3, 8, 10)
    
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

if __name__ == '__main__':
   #main(['flight', 'nightbus', 'nighttrain', 'car', 'trainferry', 'rideshare', 'plane'])
    
   # print(in_coords((49.19347, 16.61441)))
