var express = require('express');
var router = express.Router();
// const bodyParser = require('body-parser');
// const path = require('path');
// const app = express()
// const puppeteer = require('puppeteer');
// var con = require('../bin/DB/connection').connection();
// var cities = require('../bin/modules/module_city').createArrayId();
// const cors = require('cors')({origin:true});
// var request = require("request");
// var cheerio = require("cheerio");
// //const getUrls = require("get-urls");
// const fetch = require("node-fetch");

const usersRoutes = require('./users');
const mainRoutes = require('./mainRoute');

//all other code should be here
// users
router.use('/users', usersRoutes);
// mainRoutes
router.use('/', mainRoutes);

// app.use(bodyParser.json());
// app.use(bodyParser.urlencoded({ extended: false }));
// app.use(express.static(path.join(__dirname, 'public')));


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

// router.get('/getIdCities', function(req, res, next) {
//   var citiesId = cities;
//   res.json(citiesId);
// });

// function priceAndCoin(data){
//   //var befor = data;
//   setTimeout(() => {    
//     for(var i=0; i<data.length; i++){
//         var d = data[i].price;
//         //console.log(d);
//         var result = d.split("-");
//         //console.log(result);
//         var low_price = result[0];
//         //console.log(low_price);
//         //var onlyC = /[^0-9]/g;
//         //var onlyP = /[^0-9]/;
//         var p = low_price.match(/[^0-9]/g);
//         //console.log(p);
//         if(p == null||undefined||0){
//             var coin = "no price";
//         }
//         else{
//             var coin = String(p[0]);
//         }
//         //console.log(coin);
//         data[i].coin = coin;

//         var price = low_price.split(/[^0-9]/);
//         //console.log(price);
//         for(var j=0; j<price.length; j++){//run on price array
//             let priceNum = Number(price[j]);
//             if(priceNum == 0){
//                 price.splice(j, 1);
//             }
//         }

//         if(price.length == 0){
//             var finalPrice = "no price";
//         }
//         else{
//             var finalPrice = Number(price[0]);
//         }
//         //console.log(finalPrice);
//         data[i].price = finalPrice;
//         }
//         //console.log(data);
//         //res.json(data);
//         //return data;
//        },500);
//        //console.log(data);
//        //return data;

// }

// router.put('/insert_Data', function(req, res, next) {
//   var data = JSON.parse(req.body.d);
//   //var d = priceAndCoin(data);
//   //console.log(d);
//   //  setTimeout(() => {    
//   //   for(var i=0; i<data.length; i++){
//   //       var d = data[i].price;
//   //       console.log(d);
//   //       var result = d.split("-");
//   //       console.log(result);
//   //       var low_price = result[0];
//   //       console.log(low_price);
//   //       //var onlyC = /[^0-9]/g;
//   //       //var onlyP = /[^0-9]/;
//   //       var p = low_price.match(/[^0-9]/g);
//   //       console.log(p);
//   //       if(p == null||undefined||0){
//   //           var coin = "no price";
//   //       }
//   //       else{
//   //           var coin = String(p[0]);
//   //       }
//   //       console.log(coin);
//   //       data[i].coin = coin;

//   //       var price = low_price.split(/[^0-9]/);
//   //       console.log(price);
//   //       for(var j=0; j<price.length; j++){//run on price array
//   //           let priceNum = Number(price[j]);
//   //           if(priceNum == 0){
//   //               price.splice(j, 1);
//   //           }
//   //       }

//   //       if(price.length == 0){
//   //           var finalPrice = "no price";
//   //       }
//   //       else{
//   //           var finalPrice = Number(price[0]);
//   //       }
//   //       console.log(finalPrice);
//   //       data[i].price = finalPrice;
//   //       }
//   //      },500);

//   //console.log("data",data);
//   // var row_data = [];

//   // for(var i=0; i<data.length;i++){
//   //   row_data.push({from:data[i].from,to:data[i].to,type:data[i].type,time:data[i].time,price:data[i].price,coin:data[i].coin});
//   // }
  
//   //console.log("row_data",row_data);
    
//     // var add = `INSERT INTO travel_data(from,to,transportation_type,time_in_minutes,price,coin) VALUES ('${route}','${word}','${price}')`;
//     //   con.query(add,function(err,res){
//     //     if( err ){
//     //       console.log(err);
//     //       //res.send(err.message);
//     //       res.end();
//     //     }
//     //     console.log(res);
//     //     //res.send("success");
      
//     //   })
//     // console.log(row);
//     // row_data.push(row);
//   //}
//   //console.log("");
//   //var check = `SELECT route FROM travel_data WHERE route = '${r}'`;
//   res.send(data);
// })

// router.post('/getData', function(req, res, next) {
//   let from = req.body.from;
//   let to = req.body.to;
//   //let from = 'Moscow, Russia';
//   //let to = 'Paris, France';
  
//   let rome2rioUrl = 'https://www.rome2rio.com/map/'+from+'/'+to+'';
//     console.log(rome2rioUrl);
//     //.js-user-currency
//     //var user = document.querySelector('body > div.layout-header > header > div.navbar__wrapper > div.navbar__users > ul.navbar__menu > li.navbar__users-login > a.js-open-login > span.js-login-name');
//     //var user = document.querySelector('body > div.layout-header > header > div > div.navbar__users > ul > li.navbar__users-login > a > span.js-login-name').innerText;
//     //console.log(user);
//     // if (user == "SHULAMIT"){
//     //   console.log("yes");
//     // }
//     // else{
//     //   console.log("no");
//     // }  
//     (async ()=>{
         
//         const browser = await puppeteer.launch({ headless: true });
//         const page = await browser.newPage();
//         await page.setDefaultNavigationTimeout(0);
//         await page.setViewport({ width: 1920, height: 926 });
//         await page.goto(rome2rioUrl);
      
//       // get travel details
//       let travelData = await page.evaluate(() => {
      
//       let travel = [];
//       // get the travel elements
//       let travelElms = document.querySelectorAll('div.route-container')
//       //let travelElms = 
//       console.log(travelElms);
//         //get the travel data
//         travelElms.forEach((travelelement) => {
//           let wayJson = {};
//           try {
//               wayJson.types = travelelement.querySelector('h3.route__title').innerText;
//               wayJson.howMuchTypes = travelelement.querySelectorAll('ol.transit-icon > li').length;
//               var time = travelelement.querySelector('span.route__duration').innerText;
//               var time_not_point = time.split(" ");
//               wayJson.time = time_not_point[1];
//               wayJson.price =travelelement.querySelector('p > span.route__price.tip-west').innerText;
//               wayJson.coin = travelelement.querySelector('p > span.route__price.tip-west').innerText;
//             }
//           catch (exception){

//           }
//           travel.push(wayJson);
       
//       });
//         return travel;
    
//     });
   
//     res.json([travelData, from, to]);
//     })(); 
// })




module.exports = router;
