const puppeteer = require('puppeteer');
const cities = require('./api/modules/module_city').createArrayId();
const { performance } = require('perf_hooks');
const { measureMs } = require('./api/modules/measureTime');
const { ErrorHandler } = require('./api/modules/error');
const { parseTimeStr } = require('./api/modules/parseTimeStr');

const getData = async (from, to) => {
  try {
    if (!from || !to) {
      throw new ErrorHandler(400, 'missing origin and destination info');
    }

    from = setCityFromId(from);
    to = setCityFromId(to);

    let rome2rioUrl = 'https://www.rome2rio.com/map/' + from + '/' + to + '';
    console.log('starting crawler on:', rome2rioUrl);

    //start measuring time of operation:
    const start_time = performance.now();

    const browser = await puppeteer.launch({ headless: true });
    const page = await browser.newPage();
    await page.setDefaultNavigationTimeout(0);
    await page.setViewport({ width: 1920, height: 926 });
    await page.goto(rome2rioUrl);

    // get travel details
    let travelData = await page.evaluate(() => {
      let travel = [];
      // get the travel elements
      let travelElms = document.querySelectorAll('div.route-container');
      console.log('TravelController -> getData -> travelElms', travelElms);
      //get the travel data
      travelElms.forEach((travelelement) => {
        let wayJson = {};
        try {
          wayJson.title = travelelement.querySelector(
            'h3.route__title',
          ).innerText;
          wayJson.transportTypesTotal = travelelement.querySelectorAll(
            'ol.transit-icon > li',
          ).length;
          var time = travelelement.querySelector('span.route__duration')
            .innerText;
          var time_not_point = time.split(' ');
          wayJson.time = time_not_point[1];

          const priceStrData = travelelement.querySelector(
            'p > span.route__price.tip-west',
          ).innerText;
          console.log('getData -> priceStrData', priceStrData);
          wayJson.priceStrData = priceStrData;

          // wayJson.priceStrData = splitStrData(priceStrData);

          // wayJson.priceMin = travelelement
          //   .querySelector('p > span.route__price.tip-west')
          //   .innerText.split(' - ')[0]
          //   .match(/[0-9]/);

          // wayJson.priceMax = travelelement
          //   .querySelector('p > span.route__price.tip-west')
          //   .innerText.split(' - ')[1]
          //   .match(/[0-9]/)
          //   .join('');

          // wayJson.currencyName = travelelement
          //   .querySelector('p > span.route__price.tip-west')
          //   .innerText.match(/^[0-9]/);
        } catch (exception) {}
        travel.push(wayJson);
      });
      return travel;
    });

    const duration = measureMs('puppeteer', start_time);
    console.log('getData ->    { duration, travelData, from, to }', {
      duration,
      travelData,
      from,
      to,
    });
    return { duration, travelData, from, to };
  } catch (error) {
    console.log('getData -> error', error);
  }
};

const setCityFromId = (id) => {
  id = Number(id);
  if (!id) return null;
  const cityName = cities.find((entry) => entry.id === id);

  if (cityName && cityName.city) {
    return cityName.city;
  }
  return null;
};

// const data
const main = async () => {
  try {
    // call crawler
    const result = await getData(387, 225);
    if (!result.travelData) throw new Error('no price data');

    const { travelData } = result;

    for (let i = 0; i < travelData.length; i++) {
      const dataObj = result.travelData[i];
      dataObj.priceDataObj = splitStrData(travelData[i].priceStrData);

      dataObj.time_parsed = parseTimeStr(dataObj.time);
    }
    console.log('main -> result', result);
  } catch (error) {
    console.log('main -> error', error);
  }
};

const splitStrData = (str) => {
  const reply = {};

  const noNumRegex = /[^0-9]/;
  reply.coinType = str.match(noNumRegex)[0];
  const minMaxPrice = str.split(' - ');

  const onlyNumRegex = /[0-9]/g;
  reply.minPrice = minMaxPrice[0].match(onlyNumRegex).join('');
  reply.maxPrice = minMaxPrice[1].match(onlyNumRegex).join('');

  return reply;
};

main();