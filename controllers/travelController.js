const puppeteer = require('puppeteer');
var cities = require('../bin/modules/module_city').createArrayId();

class TravelController {
  //get list cities
  static getCities(req, res, next) {
    //console.log(cities);
    res.json(cities);
  }

  //get information from rome2rio
  static async getData(req, res, next) {
    try {
      let from = req.body.from;
      let to = req.body.to;
      console.log(from,to);
      let rome2rioUrl = 'https://www.rome2rio.com/map/' + from + '/' + to + '';

      console.log(rome2rioUrl);
      const browser = await puppeteer.launch({ headless: true });
      const page = await browser.newPage();

      await page.setDefaultNavigationTimeout(0);
      await page.setViewport({ width: 1920, height: 926 });
      await page.goto(rome2rioUrl);

      await page.setCookie({
        'name': 'currency',
        'value': 'EUR'
      });

      console.log(rome2rioUrl);
      // TODO: разметка HTML уже не та!!!
      // get travel details
      let travelData = await page.evaluate(() => {
        let travel = [];
        // get the travel elements
        let travelElms = document.querySelectorAll('div.route-container');
        let str = JSON.stringify(travelElms);
        console.log("travelElms", str);

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
          } catch (exception) {

          }
          travel.push(wayJson);
        });
        return travel;
      });
      
      var prices=[];
      travelData = travelData.map(dataObj => {
        let { time,coin, price } = dataObj;
        
        if (price) {
          price = price.split(' - ')[0];
          price = price.match(/\d/g);
          
          
          price = price.join("");
          
          let finalPrice = Number(price);
          
          
          dataObj.price = finalPrice;
          
          if (coin) {
            coin = coin.split(' - ')[0];
            coin = coin.replace(/[,]+/g,' ').trim();
            coin = coin.replace(/\s/g, '');
            coin = coin.match(/[^0-9\.]+/g);
            if (coin == null || undefined || 0) {
              
              dataObj.coin = "no price";
            }
            dataObj.coin = coin.join("");
            
          }
          prices.push({time,finalPrice,coin});
        }
        
        return dataObj

      })
      
      await browser.close();
      
      res.json([travelData, from, to, prices]);

    } catch (error) {
      
      next(error);
    }
  }
    
    static async createArrayAllPrices(req,res,next){  
      var from = req.body.from;
      var to = req.body.to;
      var prices = req.body.prices;
      if(from&to&prices){
        res.send("success!");
        console.log("success!");
      }
        var price = prices[0].finalPrice;
        var coin = prices[0].coin;
        var time = prices[0].time;
        var lowPricesData = {from,to,time,price,coin};
    
      res.send(isInserted);
      res.end();
  }

}



module.exports = { TravelController };