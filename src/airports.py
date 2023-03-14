import airportsdata
from pathlib import Path
from config import IATA_CODES_CSV
import pandas as pd
import pycountry


def get_airports_codes():
    IATA_CODES = Path('../files/csv')
    airports = airportsdata.load('IATA')  # key is the IATA location code
    IATA_CODES.mkdir(parents=True, exist_ok=True)
    
    with open(IATA_CODES/'iata_codes.csv', 'w') as file:
        for k, v in airports.items():
            if v['city']:
                #file.write(f"{v['iata']},{v['city']}\n")
                #print(v['iata'], v['city'])
                print(k, v)




def add_country():
    airports = airportsdata.load('IATA')
    df_iata_codes = pd.read_csv(IATA_CODES_CSV, names=['code', 'city'], index_col='code')
    for k, v in airports.items():
        df_iata_codes.at[v['iata'], 'country'] = v['country']
    df_iata_codes.to_csv(IATA_CODES_CSV, header=False)



def get_country_code():
    
    df_iata_codes = pd.read_csv(IATA_CODES_CSV, names=['code', 'city', 'country_code'], index_col='code')

    name = 'Iran'
    try:
        acountry = pycountry.countries.get(name=r'$Iran*$').alpha_2
        print('IR' in acountry)
        """ country = pycountry.countries.get(name='Iraq')
        print(country.alpha_2) """
    except Exception as err:
        print('Country not found', err, sep='\n')

    



if __name__ == '__main__':
    #get_airports_codes()
    #add_country()
  