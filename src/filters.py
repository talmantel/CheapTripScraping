from config import NOT_FOUND, BAD_VALUES
from config import EURO_ZONE, EURO_ZONE_DURATION_LIMIT, EURO_ZONE_LOWEST_PRICE
from config import ROMANIAN_CITIES, TRANS_NICOLAESCU


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


def is_trans_nicolaescu(ttype: str, transporter: str, from_id: int, to_id: int) -> bool:
    if ttype == 'bus' and transporter == TRANS_NICOLAESCU:
        if (from_id not in ROMANIAN_CITIES) or (to_id not in ROMANIAN_CITIES):
            return True
    return False

