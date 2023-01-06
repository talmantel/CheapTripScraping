from itertools import permutations


from settings import df_bb, df_airports, df_city_countries


def gen_city_country_pairs() -> tuple:
   
    union_bb_airports = set(df_bb['id_city']).union(df_airports['id_city'])
    
    intersect_city_countries_bb_airports = set(df_city_countries['id_city']).intersection(union_bb_airports)
    
    for from_id_city, to_id_city in permutations(intersect_city_countries_bb_airports, 2):
        
        # get the city name and country name
        from_city_id, from_city, from_country = df_city_countries.filter(df_city_countries['id_city'] == from_id_city).row(0)
        to_city_id, to_city, to_country = df_city_countries.filter(df_city_countries['id_city'] == to_id_city).row(0)
    
        yield from_city_id, from_city, from_country, to_city_id, to_city, to_country
    
    

if __name__ == '__main__':
    
    for _ in range(10):
        print(gen_city_country_pairs())
    """ with open('all_city_country_pairs.csv', 'w') as f:    
        for i, (from_city_id, from_city, from_country, to_city_id, to_city, to_country) in enumerate(gen_city_country_pairs()):
            f.writelines(f'{i}, {from_city_id}, {from_city}, {from_country}, {to_city_id}, {to_city}, {to_country}\n') """

    
