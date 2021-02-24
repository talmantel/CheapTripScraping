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
router.post('/insertLowPrices', TravelController.createArrayAllPrices); // было в базу данных

//create variables and edit data before insert to database

router.put('/insert_Data', function (req, res, next) {
  var data = JSON.parse(req.body.d);
  
  for (var i = 0; i < data.length; i++) {
    var from = Number(data[i].from);
    var to = Number(data[i].to);
    var type = Number(data[i].types);
    var time = Number(data[i].time);
    var price = Number(data[i].price);
    var line = data[i].line;
    var coin = data[i].coin;
    from = from ? from : 0; 
    to = to ? to : 0;
    type = type ? type : 0;
    time = time ? time : 0;
    price = price ? price : 0;
  }

  
  
  res.end();
});

router.post('/getData', TravelController.getData);



module.exports = router;
