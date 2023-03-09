from preprocessing import preprocessing
from scraping import scrap_json
from extraction import extract_data
from treatment import treat_data


def main():
    
    preprocessing()
    
    scrap_json()
    
    extract_data()
    
    treat_data()


if __name__ == '__main__':
    main()