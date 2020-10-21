const express = require('express');
const router = express.Router();
const { TravelController } = require('../controllers/travel.controller');

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/getIdCities', TravelController.getCities);

router.put('/insert_Data', function (req, res, next) {
  var data = JSON.parse(req.body.d);
  //var d = priceAndCoin(data);
  //console.log(d);
  //  setTimeout(() => {
  //   for(var i=0; i<data.length; i++){
  //       var d = data[i].price;
  //       console.log(d);
  //       var result = d.split("-");
  //       console.log(result);
  //       var low_price = result[0];
  //       console.log(low_price);
  //       //var onlyC = /[^0-9]/g;
  //       //var onlyP = /[^0-9]/;
  //       var p = low_price.match(/[^0-9]/g);
  //       console.log(p);
  //       if(p == null||undefined||0){
  //           var coin = "no price";
  //       }
  //       else{
  //           var coin = String(p[0]);
  //       }
  //       console.log(coin);
  //       data[i].coin = coin;

  //       var price = low_price.split(/[^0-9]/);
  //       console.log(price);
  //       for(var j=0; j<price.length; j++){//run on price array
  //           let priceNum = Number(price[j]);
  //           if(priceNum == 0){
  //               price.splice(j, 1);
  //           }
  //       }

  //       if(price.length == 0){
  //           var finalPrice = "no price";
  //       }
  //       else{
  //           var finalPrice = Number(price[0]);
  //       }
  //       console.log(finalPrice);
  //       data[i].price = finalPrice;
  //       }
  //      },500);

  //console.log("data",data);
  // var row_data = [];

  // for(var i=0; i<data.length;i++){
  //   row_data.push({from:data[i].from,to:data[i].to,type:data[i].type,time:data[i].time,price:data[i].price,coin:data[i].coin});
  // }

  //console.log("row_data",row_data);

  // var add = `INSERT INTO travel_data(from,to,transportation_type,time_in_minutes,price,coin) VALUES ('${route}','${word}','${price}')`;
  //   con.query(add,function(err,res){
  //     if( err ){
  //       console.log(err);
  //       //res.send(err.message);
  //       res.end();
  //     }
  //     console.log(res);
  //     //res.send("success");

  //   })
  // console.log(row);
  // row_data.push(row);
  //}
  //console.log("");
  //var check = `SELECT route FROM travel_data WHERE route = '${r}'`;
  res.send(data);
});

router.post('/getData', TravelController.getData);

module.exports = router;
