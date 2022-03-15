import requests
import logging
import json


def extract_json_from_html(trip, url, output_html_path, output_json_path, debug=0):  
    """
    extract trip options and transits json out of html
    """
    
    ## in case we ant to avoid generated json which already exists we may use below condition##
    # output_html_path = base_html_path + origin + '_' + dest + '.txt'
    # if not os.path.isfile(output_json_path):
    response = requests.get(url)

    # write url html to a file
    with open(output_html_path, 'w', encoding='utf-8') as f:
        if debug == 1:
            logging.info(f'writing to file {output_html_path}')
        f.write(response.text)

    with open(output_html_path, 'r', encoding='utf-8') as f:
        html = f.read()

    sep = r"deeplinkTrip"
    start_info = html.find(sep)
    # logging.info(start_info)
    start_quote = html.find("='", start_info)
    # logging.info(start_quote)
    end_quote = html.find('/>', start_quote)
    # logging.info(end_quote)
    body = html[start_quote + 2: end_quote-2]
    if debug == 1:
        logging.info(body)

    with open(output_json_path, 'w', encoding='utf-8') as f:
        if debug == 1:
            logging.info(f'writing to file {output_json_path}')
        f.write(body)
        
    file = 'output/json/'+trip+'.json'
