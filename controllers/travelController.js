const puppeteer = require('puppeteer');
var express = require('express');
var router = express.Router();
var cities = require('../bin/modules/module_city').createArrayId();
const con = require('../bin/DB/connection').getpool();
const { performance } = require('perf_hooks');
const { measureMs } = require('../helpers/helperFunc');

class TravelController {
  static getCities(req, res, next) {
    //console.log(cities);
    res.json(cities);
  }

  
  static async getData(req, res, next) {
    try {
      let from = req.body.from;
      let to = req.body.to;

      let rome2rioUrl = 'https://www.rome2rio.com/map/' + from + '/' + to + '';
      console.log(rome2rioUrl);

      //let t0 = performance.now();
      const browser = await puppeteer.launch({ headless: true });
      //measureMs('puppeteerLaunch', t0);

      //let t1 = performance.now();
      const page = await browser.newPage();
      //measureMs('puppeteerLaunch', t1);

      //let t2 = performance.now()
      await page.setDefaultNavigationTimeout(0);
      await page.setViewport({ width: 1920, height: 926 });
      await page.goto(rome2rioUrl);
      //measureMs('goto url', t2);

      //let t3 = performance.now();
      // get travel details
      let travelData = await page.evaluate(() => {
        let travel = [];
        // get the travel elements
        let travelElms = document.querySelectorAll('div.route-container');
        //let travelElms =
        //console.log(travelElms);
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
        console.log(travel);
        return travel;
      });
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
      res.json([travelData, from, to, prices]);

    } catch (error) {
      //console.log("TravelController -> getData -> error", error)
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
      if(from&to&prices){
        res.send("success!");
        console.log("success!")
      }
        var price = prices[0].finalPrice;
        var coin = prices[0].coin;
        var time = prices[0].time;
    lowPricesData = {from,to,time,price,coin};
    //console.log("lowPricesData befor insertSQL:",lowPricesData);
    
    const isInserted = await insertLowPricesToDB(lowPricesData);
    //console.log("isInserted_low_price", isInserted);
  res.send(isInserted);
  res.end();
  }
 
}


function insertLowPricesToDB(lowPricesData){
  //console.log("from travel controller:",lowPricesData.from);
 //return dataAllPrices;
  try {
    
    let insert_query =
      `INSERT INTO all_prices_the_travels (\`from\`,\`to\`,\`time_in_minutes\`,price,coin) VALUES`;

      insert_query +=`('${lowPricesData.from}','${lowPricesData.to}','${lowPricesData.time}','${lowPricesData.price}','${lowPricesData.coin}')`;
      //insert_query +='('+lowPricesData.from+','+lowPricesData.to+','+lowPricesData.time+','+lowPricesData.price+','+lowPricesData.coin+')';
  
    //console.log('insertLowPricesToDB -> insert_query', insert_query);

    con.getConnection(function (err, connection) {
      // console.log('insertSql -> connection', connection);
      if (err) {
        return console.error('error: ' + err);
      }

      // if you got a connection...
      connection.query(insert_query, function (err, res, fields) {
        if (err) {
          console.log('err', err);
        }
        // CLOSE THE CONNECTION
        //connection.release();
      });

      connection.release(function (err) {
        console.log('insertLowPricesToDB -> err', err);
      });
    });
    return true;
  } catch (error) {
    console.log('insertLowPricesToDB -> error', error);
    return false;
  }
}



module.exports = { TravelController };