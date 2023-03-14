import polars as pl

from config import NOT_FOUND, BAD_VALUES
from config import EURO_ZONE, EURO_ZONE_DURATION_LIMIT, EURO_ZONE_LOWEST_PRICE
from config import ROMANIAN_CITIES, TRANS_NICOLAESCU, CITIES_COUNTRIES_CSV


df_cities_countries = pl.read_csv(CITIES_COUNTRIES_CSV, has_header=False, new_columns=['id_city', 'city', 'country'])


def id_not_found(from_id: int, to_id: int) -> bool:
    if from_id == NOT_FOUND or to_id == NOT_FOUND:
        return True
    return False


def same_ids(from_id: int, to_id: int) -> bool:
    if from_id == to_id:
        return True
    return False


def currency_mismatch(currency: str, euro_rates: list) -> bool:
    if currency not in euro_rates or currency in BAD_VALUES:
        return True
    return False


def bad_price_value(price: int) -> bool:
    if price in BAD_VALUES:
        return True
    return False


# check the minimal euro zone conditions
def mismatch_euro_zone_terms(from_id: int, to_id: int, price: int, duration: int) -> bool:
    if (from_id in EURO_ZONE and to_id in EURO_ZONE) and price < EURO_ZONE_LOWEST_PRICE and duration > EURO_ZONE_DURATION_LIMIT:
        return True
    return False


# misses the routes from or to Romania by bus via transporter TRANS NICOLAESCU
def is_trans_nicolaescu(transporter: str, from_id: int, to_id: int) -> bool:
    if transporter == TRANS_NICOLAESCU:
        if (from_id not in ROMANIAN_CITIES) or (to_id not in ROMANIAN_CITIES):
            return True
    return False


# check price-duration dependence
#unreliable_buses= set()
def bus_price_below_estimate(fromm: int, to: int, price: int, duration: int) -> int:
    
    K_1, K_2, Q = 0.5385133730326261, 0.10985332568233755, 0.3
    
    estimated_price = 10**(K_1)*duration**(K_2)
    low_limit_price = round((1 - Q) * estimated_price)
    
    if price < low_limit_price: # and (fromm, to, price, duration) not in unreliable_buses:
        
        price = round(estimated_price)
        
        """ from_city = df_cities_countries.filter(pl.col('id_city') == fromm)['city'][0]
        to_city = df_cities_countries.filter(pl.col('id_city') == to)['city'][0]
        
        with open('../output/csv_output/unreliable_buses.csv', 'a') as file:
            file.write(f'{from_city},{to_city},{duration},{price},{low_limit_price}\n')
                            
        unreliable_buses.add((fromm, to, price, duration)) """
        
    return price