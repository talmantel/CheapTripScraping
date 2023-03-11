import airportsdata
from pathlib import Path


def get_airports_codes():
    IATA_CODES = Path('../files/csv')
    airports = airportsdata.load('IATA')  # key is the IATA location code
    IATA_CODES.mkdir(parents=True, exist_ok=True)
    with open(IATA_CODES/'iata_codes.csv', 'w') as file:
        for k, v in airports.items():
            if v['city']:
                file.write(f"{v['iata']},{v['city']}\n")
                print(v['iata'], v['city'])

if __name__ == '__main__':
    get_airports_codes()