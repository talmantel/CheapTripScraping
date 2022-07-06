import pandas as pd
from itertools import permutations
import geopy.distance
from currency_converter import CurrencyConverter


class Node:
    def __init__(self, long, lat, name='', is_airport=False, code=[], stype='station'):
        self.update(long, lat)
        self.stype = stype
        self.name = name
        self.is_airport = is_airport
        self.code = code
        
    def pairing(self, long, lat):
        return 0.5 * (long + lat) * (long + lat + 1) + lat
    
    def __eq__(self, N):
        if N.long == self.long and N.lat == self.lat:
            return True
        return False
    
    def dist(self, N):
        return geopy.distance.geodesic((self.long, self.lat), 
                                       (N.long, N.lat)).km
    
    def update(self, long, lat):
        self.id = self.pairing(long, lat)
        self.long = long
        self.lat = lat
        
    def describe(self):
        print("Long and Lat: ", self.long, self.lat)
        if self.is_airport:
            print("Is an Airport with code : ", self.code)
        else:
            print("Type :", self.stype)
        print("Name :", self.name)
        

class Graph:
    def __init__(self):
        self.nodes = []
        self.nodes_hashed = {}
        self.edges = {}  # add distance, price, name_src, name_dist, type and route. ID is the key
        self.routes = []
    def translate_trip(self, trip):
        # trip here is an index of Cities.routes[key]
        H = 8
        for i in range(len(trip)):
            nodes = []
            distances = []
            prices = []
            types = []  # for edges
            for j in range(len(trip[i][H])):
                if 'walk' in trip[i][H][j][:2]:
                    nodes.append(Node(trip[i][H][j][6][2],
                                      trip[i][H][j][6][3], 
                                      name=trip[i][H][j][6][1],
                                      stype=trip[i][H][j][6][0]))
                    distances.append(trip[i][H][j][5])
                    prices.append(0)
                    types.append('walk')
                    
                elif 'subway' in trip[i][H][j][:2]\
                    or 'Bus' in trip[i][H][j][:2]\
                    or 'Car' in trip[i][H][j][:2]\
                    or 'carferry' in trip[i][H][j][:2]:
                    nodes.append(Node(trip[i][H][j][6][2],
                                      trip[i][H][j][6][3], 
                                      name=trip[i][H][j][6][1],
                                      stype=trip[i][H][j][6][0]))
                    distances.append(trip[i][H][j][5])
                    types.append(trip[i][H][j][1])
                    prices.append(converter.convert(trip[i][H][j][13][0][2],
                                                    trip[i][H][j][13][0][1],
                                                    DESIRED_CUR))
                            
                elif trip[i][H][j][0] == 'flight':
                    nodes.append(Node(trip[i][H][j][2][3],
                                      trip[i][H][j][2][4], 
                                      name=trip[i][H][j][2][1], 
                                      is_airport=True,
                                      code=[trip[i][H][j][2][0]],
                                      stype='flight'))
                    distances.append(-1)
                    types.append('flight')
                    prices.append(converter.convert(trip[i][H][j][11][0][2],
                                                    trip[i][H][j][11][0][1],
                                                    DESIRED_CUR))
            
            if len(nodes):
                self.nodes.append(nodes[0])
                self.nodes_hashed[nodes[0].id] = nodes[0]
                tmp_route = [nodes[0]]
                for n in range(1, len(nodes)):
                    nodes.append(nodes[n])
                    if distances[n] == -1:
                        tmp = nodes[n].dist(nodes[n-1])
                    else:
                        tmp = distances[n]
                    self.edges[(nodes[n-1].id, nodes[n].id)] =\
                            (tmp, prices[n-1], nodes[n-1].name, nodes[n].name, types[n-1], i)
                    self.nodes.append(nodes[n])
                    self.nodes_hashed[nodes[n].id] = nodes[n]
                    tmp_route.append(nodes[n].id)
                    
            
    def visalize(self):
        points = []
        for n in range(len(self.nodes)):
            points.append(Point(self.nodes[n].lon, self.nodes[n].lat))
        return [Point(xy) for xy in zip(df['Longitude'], df['Latitude'])]
        