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
    print('start')
    for code in df_iata_codes.index.values:
        try:
            country_name = pycountry.countries.get(alpha_2=df_iata_codes.at[code, 'country_code']).name
            print(country_name)
            df_iata_codes.at[code, 'country'] = country_name
    
        except Exception as err:
            print(f'Country for code {code} not found', err, sep='\n')
            continue
    df_iata_codes.to_csv(IATA_CODES_CSV, header=False)
    print(df_iata_codes.head())
    



if __name__ == '__main__':
    #get_airports_codes()
    #add_country()
    get_country_code()
    pass