import csv
import logging


def gen_cities_combination(cities_path, debug=False, limit_cities=100, limit_combination=50, filter_diff_lat=0, filter_diff_lng=0):
    """
    desc: load cities csv from a given file path
          if debug passing logging.infos additional info
          currently it filters only combination of cities wich are far from each other (using lng and lat)
    returns: tuples list of cities combination according to given limit
             if limit is not passing will use a default
    """

    with open(cities_path, "r", encoding="utf-8") as f:
        cities = f.read()

    ls_cities = []
    ls_filterd_cities = []
    with open(cities_path, "r", encoding="utf-8") as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                # diplay csv file header
                if debug == True:
                    print(f'Column names are: {", ".join(row)}')
                line_count += 1
            else:
                # map csv city, country, lat, lng,
                # city = [row[4]+' '+row[0]] # EasyPNR-Airports.csv
                # city = [row[1], row[7], row[3], row[4]] # Full_list_with_countries_updated_no_Donetsk

                # in case airplane code column length is equals to 3 - meaning only 1 airport
                if len(row[2]) == 3:
                    city = [row[0], row[1], row[2], row[3], row[4]]  # Locations with airports.csv
                    # add current city to ls_cities dictionary
                    ls_cities.append(city)
                if len(row[2]) == 4 and row[3]:
                    # split given airplane codes in case airplane code column length is 4 (multiple airplane) and column3 is not empty
                    ls_mutlipleAirPorts = row[3].split(',')
                    # loop over column3 and add each airplane code as a new list
                    for ap_code in ls_mutlipleAirPorts:
                        city = [row[0], row[1], ap_code, row[2], row[4]]  # Locations with airports.csv
                        # add current city to ls_cities dictionary
                        ls_cities.append(city)
                        line_count += 1
                else:
                    # print(f'airport code column {len(row[2])} has nonvalid length')
                    continue
                line_count += 1
        if debug == True:
            print(f'found total of {len(ls_cities)} cities in given file')
            print([city[0] for city in ls_cities])
        # limit cities according to given limit_cities
        ls_cities = ls_cities[:limit_cities]
        if debug == True:
            print(f'found total of {len(ls_cities)} cities in given file according to given limit_cities argument')
            print([city[0] for city in ls_cities])
        # create a cartisian list of cities
        ls_cities_comb = [[city1[0], city2[0]] for city1 in ls_cities for city2 in ls_cities if city1[0] != city2[0]]
        if debug == True:
            print(f'found total of {len(ls_cities_comb)} cities combinations')
            print(ls_cities_comb)
            print(ls_cities)
        # loop over the cartisian list of cities
        # in order to generate a new list of cartisian cities including its attributes filtered by geo distance and limit_combination
        for i, city_comb in enumerate(ls_cities_comb):
            # loop 1 over list of the city attributes loaded from the file
            for n, city1 in enumerate(ls_cities):
                # loop 2 over list of the city attributes loaded from the file
                for city2 in ls_cities:
                    # prepare list for
                    ls_cities_combination = [city1, city2]
                    # avoid same city
                    if city1 != city2:
                        # check for only cities metched with cities from the cartisian list
                        if city1[0] == city_comb[0] and city2[0] == city_comb[1]:
                            # check only if any geo condition is exists
                            if filter_diff_lat != 0 or filter_diff_lng != 0:
                                # evaluate absolute distance of longitude and latitude of cities sets
                                diff_lat = abs(float(city1[2]) - float(city2[2]))
                                diff_lng = abs(float(city1[3]) - float(city2[3]))
                                if diff_lat >= filter_diff_lat and diff_lng >= filter_diff_lng:
                                    # append only those cities that metch the geo criteria
                                    ls_filterd_cities.append(ls_cities_combination)
                            else:
                                # append cities sets to ls_filterd_cities in case there is no geo criteria
                                ls_filterd_cities.append(ls_cities_combination)
        if debug == True:
            print(f'total of {len(ls_filterd_cities)} cities were found according to given coordinates')
            print(ls_filterd_cities)

        ls_filterd_cities = ls_filterd_cities[:limit_combination]
        if debug == True:
            print(f'total of {len(ls_filterd_cities)} cities were found according to given limit_combination argument')
            print(ls_filterd_cities)

        return ls_filterd_cities

# debug function
# cities_path = r'C:\Eran\DataEngineering\Project\cheaptrip\files\csv\Locations with airports.csv'
# tp_cities = gen_cities_combination(cities_path, debug=False, limit_cities=25, limit_combination=1000, filter_diff_lat=0, filter_diff_lng=0)
# print(tp_cities)