import pandas as pd
from config import RAW_CSV, VALIDATION_CSV, TRIPLES_CSV


def treat_data():
    
    print('Start treatment process...')
    
    # Reading raw csv
    df_raw= pd.read_csv(RAW_CSV, index_col=None, 
                        usecols=['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min', 'distance_km', 'frequency_tpw'])

    # Removing full duplicates
    df_val = df_raw.drop_duplicates(ignore_index=True) 
    
    # Write to csv for validation purposes
    df_val.to_csv(VALIDATION_CSV, index=False)
    
    # Sorting by price in ascending order
    df = df_val.sort_values(by=['from_id', 'to_id', 'transport_id', 'price_min_EUR'], 
                            ignore_index=True, 
                            ascending=True)

    # Removing duplicates by triples ('from_id', 'to_id', 'transport_id')
    df = df.drop_duplicates(['from_id', 'to_id', 'transport_id'], ignore_index=True)

    # Create index in resulting csv files and cutting small files 'from_id.csv' type
    frames = []
    for from_id in df['from_id'].unique():
        
        temp_df = df[df['from_id'] == from_id]
        
        temp_df.index = from_id * 10_000 + range(1, temp_df.shape[0] + 1)
        
        temp_df.index.name = 'path_id'
        
        #temp_df.to_csv(f'{TARGET_DIR}/{from_id}.csv') # small csv files cutting
        
        frames.append(temp_df)
        
    df_triples = pd.concat(frames)

    # Output 
    df_triples.to_csv(TRIPLES_CSV, columns=['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min'])
    
    print('Data treatment finished successfully!\n')
    
    
if __name__ == '__main__':
    
    treat_data()