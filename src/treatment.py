import pandas as pd
from config import RAW_CSV, VALIDATION_CSV, TRIPLES_CSV, OUTPUT_COLUMNS


def treat_data():
    
    print('\nData treatment...', end='...')
    
    # Making validation dataset from raw csv
    df_raw= pd.read_csv(RAW_CSV, index_col=0, dtype= {'from_id': 'Int32', 'to_id': 'Int32', 'transport_id': 'Int32', 'price_min_EUR': 'Int32', 
                                 'duration_min': 'Int32', 'distance_km': 'Int32', 'frequency_tpw': 'Int32', 'num_transfers':'Int32'})
    
    # Write to csv for validation purposes
    df_raw.to_csv(VALIDATION_CSV, index=False, columns=['from_id', 'to_id', 'transport_id', 'price_min_EUR', 
                                 'duration_min', 'distance_km', 'frequency_tpw', 'num_transfers'])
        
    # Making dataset for triples
    df_triples = df_raw[['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min', 'num_transfers']].copy()
    
    # Sorting by price in ascending order
    df_triples.sort_values(by=['from_id', 'to_id', 'transport_id', 'price_min_EUR'], ascending=True, inplace=True)

    # Removing duplicates by triples ('from_id', 'to_id', 'transport_id')
    df_triples = df_triples.drop_duplicates(['from_id', 'to_id', 'transport_id']).sort_values(by='path_id')
    
    # adding of lacking backward routes for direct flights or share
    df_specific = df_triples.query('(transport_id == 1 and num_transfers == 0) or transport_id == 8')
    for index in df_specific.index:
        from_id, to_id = df_specific.loc[index, ['from_id', 'to_id']]
        
        # if there is a reverse route takes next pair (from_id, to_id)
        if not df_specific.query('from_id == @to_id and to_id == @from_id').empty: continue
        
        # creates a path_id for the given reverse route
        path_id = 1 + max(filter(lambda x: x > to_id * 10_000 and x < (to_id + 1 ) * 10_000 , df_triples.index.values))
        
        # add reverse routes to the dataset
        df_triples.at[path_id, 'from_id'] = to_id
        df_triples.at[path_id, 'to_id'] = from_id
        df_triples.at[path_id, 'transport_id'] = df_triples.at[index, 'transport_id']
        df_triples.at[path_id, 'price_min_EUR'] = df_triples.at[index, 'price_min_EUR']
        df_triples.at[path_id, 'duration_min'] = df_triples.at[index, 'duration_min']
        df_triples.at[path_id, 'num_transfers'] = df_triples.at[index, 'num_transfers']
    
    # Output to csv
    df_triples.sort_index(inplace=True)
    df_triples.to_csv(TRIPLES_CSV)
    
    print('successfully!\n')
    
    
if __name__ == '__main__':
    
    treat_data()