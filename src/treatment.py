import pandas as pd
from config import OUTPUT_CSV_DIR, raw_csv


def treat_data():
    
    print('Start treatment process...')
    
    # 1. Reading raw csv
    df_raw= pd.read_csv(OUTPUT_CSV_DIR/raw_csv, index_col=None, 
                        usecols=['from_id', 'to_id', 'transport_id', 'price_min_EUR', 'duration_min'])

    # 2. Removing full duplicates
    df_val = df_raw.drop_duplicates(ignore_index=True)
    
    # 3a. Filter applying
    # set up limits
    euro_zone_codes = range(100, 371)
    price_limit, duration_limit = 5, 60
    # set up conditions
    in_euro_zone = 'from_id in @euro_zone_codes and to_id in @euro_zone_codes'
    price_less_limit = 'price_min_EUR < @price_limit'
    duration_more_limit = 'duration_min > @duration_limit'
    # applying filter
    filter = df_val.query(in_euro_zone + ' and ' + price_less_limit + ' and ' + duration_more_limit)

    # 3b. Drop filtered rows
    df_val_filtered = df_val.drop(filter.index, axis=0) 
    
    # 3c. Write to csv
    df_val_filtered.to_csv(OUTPUT_CSV_DIR/'all_direct_routes_validation.csv', index=False)
    
    # 4. Sorting by price in ascending order
    df = df_val.sort_values(by=['from_id', 'to_id', 'transport_id', 'price_min_EUR'], 
                            ignore_index=True, 
                            ascending=True)

    # 5. Removing duplicates by triples ('from_id', 'to_id', 'transport_id')
    df = df.drop_duplicates(['from_id', 'to_id', 'transport_id'], ignore_index=True)

    # 6. Create index in resulting csv files and cutting small files 'from_id.csv' type
    frames = []
    for from_id in df['from_id'].unique():
        
        temp_df = df[df['from_id'] == from_id]
        
        temp_df.index = from_id * 10_000 + range(1, temp_df.shape[0] + 1)
        
        temp_df.index.name = 'path_id'
        
        #temp_df.to_csv(f'{TARGET_DIR}/{from_id}.csv') # small csv files cutting
        
        frames.append(temp_df)
        
    res_df = pd.concat(frames)

    res_df.to_csv(f'{OUTPUT_CSV_DIR}/all_direct_routes_triples.csv')
    
    print('Data treatment finished successfully!\n')
    
    
if __name__ == '__main__':
    
    treat_data()