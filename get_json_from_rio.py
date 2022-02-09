import requests
import logging
import concurrent.futures
import time
import compress_json



def timeit(method):
    def timed(*args, **kw):
        ts = time.time()
        result = method(*args, **kw)
        te = time.time()
        if 'log_time' in kw:
            name = kw.get('log_name', method.__name__.upper())
            kw['log_time'][name] = int((te - ts) * 1000)
        else:
            print('%r  %2.2f ms' % \
                  (method.__name__, (te - ts) * 1000))
        return result

    return timed


title = []
urls = []
city_list = []
city_data_list = []


@timeit
def getjson():
    with open(
            r'..\files\output\csv_bbox\bbox_out.csv',
            'r') as file:
        start = int(input("from which id get json ?"))
        finish = int(input('to ?'))
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
            if (item[0]) >= start and item[0] <= finish:
                city_data_list.append([item[0], item[1], item[6]])

            city_list.append([item[0], item[1], item[6]])


getjson()


@timeit
def create_url(city_data_list, city_list):
    for i in city_data_list:

        for j in city_data_list:
            if i[1] != j[1]:
                urls.append(f'https://www.rome2rio.com/map/{i[1]}-{i[2]}/{j[1]}-{j[2]}')
                print(f'https://www.rome2rio.com/map/{i[1]}-{i[2]}/{j[1]}-{j[2]}')


create_url(city_data_list, city_list)


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



    compress_json.local_dump(body,
                             f"../output_json/{url.partition('map/')[2]}.json.gz")





@timeit
def gen_iter():
    with concurrent.futures.ThreadPoolExecutor() as  executor:
        executor.map(get_requsests, urls,timeout=60)



gen_iter()

