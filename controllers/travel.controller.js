const puppeteer = require('puppeteer');
const cities = require('../bin/modules/module_city').createArrayId();
const { performance } = require('perf_hooks');
const { measureMs } = require('../helpers/HelperFuncs');

class TravelController {
  static getCities(req, res, next) {
    res.json(cities);
  }
  static async getData(req, res, next) {
    try {
      let from = req.body.from;
      let to = req.body.to;

      let rome2rioUrl = 'https://www.rome2rio.com/map/' + from + '/' + to + '';
      console.log(rome2rioUrl);

      let t0 = performance.now();
      const browser = await puppeteer.launch({ headless: true });
      measureMs('puppeteerLaunch', t0);

      let t1 = performance.now();
      const page = await browser.newPage();
      measureMs('puppeteerLaunch', t1);

      let t2 = performance.now();
      await page.setDefaultNavigationTimeout(0);
      await page.setViewport({ width: 1920, height: 926 });
      await page.goto(rome2rioUrl);
      measureMs('goto url', t2);

      let t3 = performance.now();
      // get travel details
      let travelData = await page.evaluate(() => {
        let travel = [];
        // get the travel elements
        let travelElms = document.querySelectorAll('div.route-container');
        //let travelElms =
        console.log(travelElms);
        //get the travel data
        travelElms.forEach((travelelement) => {
          let wayJson = {};
          try {
            wayJson.types = travelelement.querySelector(
              'h3.route__title',
            ).innerText;
            wayJson.howMuchTypes = travelelement.querySelectorAll(
              'ol.transit-icon > li',
            ).length;
            var time = travelelement.querySelector('span.route__duration')
              .innerText;
            var time_not_point = time.split(' ');
            wayJson.time = time_not_point[1];
            wayJson.price = travelelement.querySelector(
              'p > span.route__price.tip-west',
            ).innerText;
            wayJson.coin = travelelement.querySelector(
              'p > span.route__price.tip-west',
            ).innerText;
          } catch (exception) {}
          travel.push(wayJson);
        });
        return travel;
      });
      measureMs('travelData generation', t3);

      measureMs('Total operation timw', t0);
      res.json([travelData, from, to]);
    } catch (error) {
      next(error);
    }
  }
}

module.exports = { TravelController };
