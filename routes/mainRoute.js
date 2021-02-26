const express = require('express');
const router = express.Router();
const { TravelController } = require('../controllers/travelController');
const fs = require('fs');

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/getIdCities', TravelController.getCities);

// пока непонятно, что делать с этой функцией
//router.post('/insertLowPrices', TravelController.createArrayAllPrices); // было в базу данных

//create variables and edit data before insert to database

router.put('/insert_Data', function (req, res, next) {
  let body = req.body;
  let data = [];
  data.push(body['d']);
  let jsonData = [];

  for (let i = 0; i < data.length; i++) {
    let from = Number(data[i].from);
    let to = Number(data[i].to);
    let type = Number(data[i].type);
    let time = Number(data[i].time);
    let price = Number(data[i].price);
    let line = data[i].line;
    let coin = data[i].coin;
    from = from ? from : 0; 
    to = to ? to : 0;
    type = type ? type : 0;
    time = time ? time : 0;
    price = price ? price : 0;
    let item = `${from},${to},${type},${time},${price},${line},${coin}`;
    console.log('item', item);
    console.log('jsonData', jsonData);
    //jsonData[i].push(item); // TODO: здесь не выполняется!
    console.log('jsonData', jsonData);
  }
  console.log('after', jsonData);
  jsonData = JSON.stringify(jsonData, null, 1);
  // create new JSON
  fs.writeFileSync('output.json', jsonData, function(err){
    console.log(err.message);
  });

  
  
  
  res.end();
});

router.post('/getData', TravelController.getData);



module.exports = router;
