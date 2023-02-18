import requests
import json
from pathlib import Path


def update_exchange_rates():
    target_dir = Path('../files/currencies')
    url = 'https://api.currencyapi.com/v3/latest'
    pars = {'apikey': 'XdHFU6mfUp0T3E7EQe0SNtGTfdE0JLgaxz8FDrSf', 'base_currency': 'EUR'}
    
    r = requests.get(url, params=pars)
    
    target_dir.mkdir(parents=True, exist_ok=True)
    with open(target_dir/'exchange_rates_EUR.json', mode='w') as f:
        json.dump(r.json(), f)


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
    #update_exchange_rates()
    #last_hrk_eur_rates()
    pass