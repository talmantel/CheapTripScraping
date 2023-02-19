import requests
import json
from pathlib import Path
import logging
from datetime import datetime

from config import LOGS_DIR, CURRENCY_JSON, CURRENCY_EXCHANGE_RATES_DIR

logging.basicConfig(filename=LOGS_DIR/'currency_exchange.log', filemode='w', 
                    format='%(name)s - %(levelname)s - %(message)s')

def update_exchange_rates() -> bool:
    url = 'https://api.currencyapi.com/v3/latest'
    pars = {'apikey': 'XdHFU6mfUp0T3E7EQe0SNtGTfdE0JLgaxz8FDrSf', 'base_currency': 'EUR'}
       
    try:
        r = requests.get(url, params=pars)
    
        CURRENCY_EXCHANGE_RATES_DIR.mkdir(parents=True, exist_ok=True)
        with open(CURRENCY_JSON, mode='w') as f:
            json.dump(r.json(), f)
        
        return True
    
    except Exception as err:
        print(err)
        logging.error(f'{datetime.today()} an exception occurred', exc_info=True)
        return False
        


def last_hrk_eur_rates():
    target_dir = Path('../files/currencies')
    url = 'https://api.currencyapi.com/v3/historical'
    pars = {'apikey': 'XdHFU6mfUp0T3E7EQe0SNtGTfdE0JLgaxz8FDrSf',
            'base_currency': 'EUR',
            'date': '2023-01-14',
            'currencies': ('HRK')}
    
    r = requests.get(url, params=pars)
    
    target_dir.mkdir(parents=True, exist_ok=True)
    with open(target_dir/'last_HRK_EUR_rates.json', mode='w') as f:
        json.dump(r.json(), f)
    


if __name__ == '__main__':
    update_exchange_rates()
    #last_hrk_eur_rates()
    
    pass