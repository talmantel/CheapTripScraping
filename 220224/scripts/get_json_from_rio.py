import requests
import logging
import concurrent.futures
import time
import compress_json



title = []
urls = []
city_list = []
city_data_list = []



def getjson():
    with open(
            r'../files/output/csv_bbox/bbox_out.csv',
            'r') as file:
        start = int(8)
        finish = int(2023)
        for item in file.readlines():
            item = item.split(',')
            if len(item[0]) == 0:
                continue
            item[0] = int(item[0])
            if '\'' in item[1]:
                item[1] = item[1].replace('\'', "-")

            if len(item[1].split()) > 1:
                item[1] = item[1].replace(" ", "-")
            if '\'' in item[6]:
                item[6] = item[6].replace('\'', "-")

            if len(item[6].split()) > 1:
                item[6] = item[6].replace(" ", "-")
            if int(item[0]) >= (start) and int(item[0]) <= finish:
                city_list.append([item[0], item[1], item[6]])

            city_data_list.append([item[0], item[1], item[6]])





def create_url(city_data_list, city_list):
    for i in city_list:

        for j in city_data_list:
            if i[1] != j[1]:
                urls.append(f'https://www.rome2rio.com/map/{i[1]}-{i[2]}/{j[1]}-{j[2]}')




def get_requsests(urls):
    time.sleep(2)
    url = urls
    print(url)
    response = requests.get(url)
    data = response.text
    sep = r"deeplinkTrip"
    start_info = data.find(sep)
    # # logging.info(start_info)
    start_quote = data.find("='", start_info)
    # # logging.info(start_quote)
    end_quote = data.find('/>', start_quote)
    # # logging.info(end_quote)
    body = data[start_quote + 2: end_quote - 2]




    compress_json.local_dump(body, f"../output/{url.partition('map/')[2]}.json.gz")





def gen_iter():
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as  executor:
        executor.map(get_requsests, urls,timeout=60)




if __name__=="__main__":
    getjson()
    create_url(city_data_list, city_list)
    gen_iter()