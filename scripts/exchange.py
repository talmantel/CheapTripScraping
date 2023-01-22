import requests
import json
from pathlib import Path


def get_exchange_rates():
    target_dir = Path('../files/currencies')
    url = 'https://api.currencyapi.com/v3/latest'
    pars = {'apikey': 'XdHFU6mfUp0T3E7EQe0SNtGTfdE0JLgaxz8FDrSf', 'base_currency': 'EUR'}
    
    r = requests.get(url, params=pars)
    
    target_dir.mkdir(parents=True, exist_ok=True)
    with open(target_dir/'exchange_rates_EUR.json', mode='w') as f:
        json.dump(r.json(), f)


if __name__ == '__main__':
    get_exchange_rates()