const express = require('express');
const router = express.Router();
const con = require('../bin/DB/connection').getpool();
const { TravelController } = require('../controllers/travelController');
//const { response } = require('../app');

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/getIdCities', TravelController.getCities);

router.put('/insertLowPricesToDB', TravelController.createArrayAllPrices);

router.put('/insert_Data', function (req, res, next) {
  var data = JSON.parse(req.body.d);
  //console.log('data:', data);
  const save_data = [];
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
    
    save_data.push({ from, to, type, line, time, price, coin });
  }
  const isInserted = insertSql(save_data, res);
  //console.log("isInserted", isInserted);
  res.send(isInserted);
  res.end();
});

// function sleep(ms) {
//   console.log('now sleep');
//   return new Promise((resolve) => setTimeout(resolve, ms));
// }
//await new Promise(r => setTimeout(r, 2000));
// async function demo() {
//   console.log('Taking a break...');
//   await sleep(2000);
//   console.log('Two seconds later, showing sleep in a loop...');

//   // Sleep in loop
//   for (let i = 0; i < 5; i++) {
//     if (i === 3)
//       await sleep(2000);
//     console.log(i);
//   }
// }

// demo();

//router.put('/insertCityToDB', TravelController.insertCityToDB);

router.post('/getData', TravelController.getData);

function insertSql(data_arr) {
  //console.log("data-arr:",data_arr);
  try {
    let insert_query =
      'INSERT INTO travel_data(`from`, `to`, `transportation_type`, `line`, `time_in_minutes`, `price`, `coin`) VALUES ';

    for (let i = 0; i < data_arr.length; i++) {
      let { from, to, type, line, time, price, coin } = data_arr[i];
      line = line ? line : null;
      //   insert_query +=
      //     '(' +
      //     from +
      //     ',' +
      //     to +
      //     ',' +
      //     type +
      //     ',' +
      //     time +
      //     ',' +
      //     price +
      //     ',' +
      //     '"' +
      //     coin +
      //     '"' +
      //     '),';
      // }
      insert_query +=
        '(' +
        from +
        ',' +
        to +
        ',' +
        type +
        ',' +
        '"' +
        line +
        '"' + 
        ','+
        time +
        ',' +
        price +
        ',' +
        '"' +
        coin +
        '"' +
        '),';
    }
    //console.log('insertSql -> insert_query', insert_query);
    insert_query = insert_query.slice(0, -1); //removed last sign ,
    //console.log('insertSql -> insert_query', insert_query);
    insert_query += ';';
    //console.log('insertSql -> insert_query', insert_query);

    con.getConnection(function (err, connection) {
      // console.log('insertSql -> connection', connection);
      if (err) {
        return console.error('error: ' + err);
      }

      // if you got a connection...
      connection.query(insert_query, function (err, res, fields) {
        //console.log({ err, res, fields });
        if (err) {
          //connection.release();
          console.log('err', err);
        }
        // CLOSE THE CONNECTION
        //connection.release();
      });

      connection.end(function (err) {
        console.log('insertSql -> err', err);
      });
    });
    return true;
  } catch (error) {
    console.log('insertSql -> error', error);
    return false;
  }
}

// function insertLowPricesToDB(dataAllPrices){
//   console.log("from mainRoute:",dataAllPrices);
// }

module.exports = router;
