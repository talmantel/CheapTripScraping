import pandas as pd
from config import RAW_CSV, VALIDATION_CSV, TRIPLES_CSV


def treat_data():
    
    print('\nData treatment...', end='...')
    
    # Making validation dataset from raw csv
    df_val= pd.read_csv(RAW_CSV, index_col=None, 
                        usecols=['from_id', 'to_id', 'transport_id', 'price_min_EUR', 
                                 'duration_min', 'distance_km', 'frequency_tpw'])
    
    # Write to csv for validation purposes
    df_val.to_csv(VALIDATION_CSV, index=False)
        
    # Making dataset for triples from raw csv
    df_triples = pd.read_csv(RAW_CSV, usecols=['path_id', 'from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min'])
    
    # Sorting by price in ascending order
    df_triples.sort_values(by=['from_id', 'to_id', 'transport_id', 'price_min_EUR'], ascending=True, inplace=True)

    # Removing duplicates by triples ('from_id', 'to_id', 'transport_id')
    df_triples = df_triples.drop_duplicates(['from_id', 'to_id', 'transport_id']).sort_values(by='path_id')
    
    # Output 
    df_triples.to_csv(TRIPLES_CSV, index=False)
    
    print('successfully!\n')
    
    
if __name__ == '__main__':
    
    treat_data()