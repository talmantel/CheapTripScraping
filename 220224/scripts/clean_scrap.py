import pandas as pd
from itertools import permutations
import requests
from bs4 import BeautifulSoup
import json

base_url = 'https://www.rome2rio.com/map/'
cities_file = "../files/csv/Locations with airports.csv"


def get_url(url, english=True):
    if english:
        r = requests.get(url, headers={'Accept-Language': 'en-US,en;q=0.5'})
    else:
        r = requests.get(url)

    if r.status_code != 200:
        print("Invalid page!")
        return []
    else:
        data = {}
        soup = BeautifulSoup(r.content, 'html.parser')
        # data["title"] = soup.find('h1').text
        dis = soup.find('meta', {'id': 'deeplinkTrip'})
        parsed = json.loads(dis["content"])[2][1][0]

        # print(json.dumps(parsed, indent=4, sort_keys=True))
        return parsed


class Cities:
    def __init__(self, filename):
        self.path = cities_file
        self.base_url = 'https://www.rome2rio.com/map/'
        try:
            self.df = pd.read_csv(filename)
        except FileNotFoundError:
            print("File Not Found")
        self.routes = {}

    def gen_combinations(self, n=2):
        permutations(self.df['city'])

    def get_routes(self, city1, city2):
        pass

    def scrap_routes(self, city1, city2):
        tmp_url = self.base_url + city1 + '/' + city2
        self.routes[(city1, city2)] = get_url(tmp_url)


c = Cities(cities_file)
print(c.df.head())
#c.scrap_routes(c.df.iloc[0]['city'], c.df.iloc[1]['city'])
c.scrap_routes('Saint-Petersburg', 'London')
c.routes