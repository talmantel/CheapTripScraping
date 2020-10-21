const puppeteer = require('puppeteer');
var cities = require('../modules/module_city').createArrayId();
const con = require('../DB/connection').getpool();
//const { performance } = require('perf_hooks');
//const { measureMs } = require('../helpers/helperFunc');*
const { performance } = require('perf_hooks');
const { measureMs } = require('../modules/measureTime');//*
const { ErrorHandler } = require('../modules/error');
const TravelData = require('../models/TravelData');


class TravelController {
  // static getCities(req, res, next) {
  //   //console.log(cities);
  //   res.json(cities);
  // }

  static getIdCities(req, res, next) {
    //console.log(cities);
    let fromToArray = [];
//need change function from:barcelona to:barcelona its no good
    cities.forEach((fromCity) => {
      cities.forEach((toCity) => {
        if(fromCity!=toCity){
        fromToArray.push({ from: fromCity.id, to: toCity.id });
        }
      });
    });
    //console.log(fromToArray);
    res.json({ results_fromToArray: fromToArray.length, cities, fromToArray });
  }

  static async getData(req, res, next) {
    try {
      let from = req.body.from;
      let to = req.body.to;
      console.log(from,to)
      if (!from || !to) {
        throw new ErrorHandler(400, 'missing origin and destination info');
      }

      from = setCityFromId(from);//word
      to = setCityFromId(to);//word

      if (!from || !to) {
        const raw_from_value = req.body.from;
        const raw_to_value = req.body.to;

        throw new ErrorHandler(
          400,
          `city id not registered {from:${raw_from_value},to:${raw_to_value}}`,
        );
      }
      let rome2rioUrl = 'https://www.rome2rio.com/map/' + from + '/' + to + '';
      //console.log(rome2rioUrl);
      console.log('starting crawler on:', rome2rioUrl);

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
        console.log('TravelController -> getData -> travelElms',travelElms);
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

      const duration = measureMs('puppeteer', start_time);
      //measureMs('travelData generation', t3);

      //measureMs('Total operation timw', t0);
      //console.log("travelData:",travelData);
      var prices=[];
      travelData = travelData.map(dataObj => {
        let { time,coin, price } = dataObj;
        
        if (price) {
          price = price.split(' - ')[0];
          price = price.match(/\d/g);
          //console.log("price", price, typeof price);
          
          price = price.join("");
          //console.log(price, typeof price);//this is String
          let finalPrice = Number(price);
          //console.log(finalPrice, typeof finalPrice);//this is Number
          
          dataObj.price = finalPrice;
          //}
          //console.log("travel controller -> coin: ",coin);
          if (coin) {
            coin = coin.split(' - ')[0];
            //console.log(coin);
            coin = coin.replace(/[,]+/g,' ').trim();
            coin = coin.replace(/\s/g, '');
            coin = coin.match(/[^0-9\.]+/g);
            if (coin == null || undefined || 0) {
              //console.log("no price", coin);
              dataObj.coin = "no price";
            }
            dataObj.coin = coin.join("");
            //console.log(dataObj.coin, typeof dataObj.coin);//this is string
          }
          prices.push({time,finalPrice,coin});
        }

        return dataObj
        //return dataObj

      })
      await browser.close();
      //console.log("TravelController -> getData -> travelData", travelData)
      res.json([duration, travelData, from, to, prices]);

    } catch (error) {
      //console.log("TravelController -> getData -> error", error)
      next(error);
    }
  }

  static async getAll(req, res, next) {
    try {
      let result = await TravelData.getAll();
      res.json({ result });
    } catch (error) {
      next(error);
    }
  }

  //static async insertLowPricesToDB(req,res,next){
    static async createArrayAllPrices(req,res,next){  
      var from = req.body.from;
      var to = req.body.to;
      var prices = req.body.prices;
        var lowPricesData = {};
        //console.log("from:",from,"to:",to,"prices:",prices);
      if(from&to&prices){res.send("success!");console.log("success!")}
        var price = prices[0].finalPrice;
        var coin = prices[0].coin;
        var time = prices[0].time;
    lowPricesData = {from,to,time,price,coin};
    console.log("lowPricesData:",lowPricesData);
    
    //const isInserted = await insertLowPricesToDB(lowPricesData, res);
  //console.log("isInserted_low_price", isInserted);
  //res.send(isInserted);
  //res.end();
  } 

}//close travel controler

function setCityFromId(id) {
  //console.log(cities);
  id = Number(id);
  if (!id) return null;
  const cityName = cities.find((entry) => entry.id === id);

  if (cityName && cityName.city) {
    return cityName.city;
  }
  return null;
}

function insertLowPricesToDB(lowPricesData){
  console.log("from travel controller:",lowPricesData);
 //return dataAllPrices;
  try {
    
    let insert_query =
      `INSERT INTO all_prices_the_travels('from', 'to', 'time_in_minutes', 'price', 'coin')VALUES`;

      insert_query +=`(${from},${to},${time},${price},${coin})`;
    // for (let i = 0; i < dataAllPrices.length; i++) {
    //   let { from, to, price, coin } = dataAllPrices[i];
      // insert_query +=
      //   '(' +
      //   from +
      //   ',' +
      //   to +
      //   ',' +
      //   time +
      //   ','
      //   price +
      //   ',' +
      //   '"' +
      //   coin +
      //   '"' +
      //   ')';
    // }
    console.log('insertLowPricesToDB -> insert_query', insert_query);
    //insert_query = insert_query.slice(0, -1); //removed last sign ,
    console.log('insertLowPricesToDB -> insert_query', insert_query);
    //insert_query += ';';
    console.log('insertLowPricesToDB -> insert_query', insert_query);

    // con.getConnection(function (err, connection) {
    //   // console.log('insertSql -> connection', connection);
    //   if (err) {
    //     return console.error('error: ' + err);
    //   }

    //   // if you got a connection...
    //   connection.query(insert_query, function (err, res, fields) {
    //     //console.log({ err, res, fields });
    //     if (err) {
    //       //connection.release();
    //       console.log('err', err);
    //     }
    //     // CLOSE THE CONNECTION
    //     //connection.release();
    //   });

    //   connection.end(function (err) {
    //     console.log('insertLowPricesToDB -> err', err);
    //   });
    // });
    // return true;
  } catch (error) {
    console.log('insertLowPricesToDB -> error', error);
    return false;
  }
}



module.exports = { TravelController };