const { performance } = require('perf_hooks');

module.exports = {
  priceAndCoin: (data) => {
    //var befor = data;
    setTimeout(() => {
      for (var i = 0; i < data.length; i++) {
        var d = data[i].price;
        //console.log(d);
        var result = d.split('-');
        //console.log(result);
        var low_price = result[0];
        //console.log(low_price);
        //var onlyC = /[^0-9]/g;
        //var onlyP = /[^0-9]/;
        var p = low_price.match(/[^0-9]/g);
        //console.log(p);
        if (p == null || undefined || 0) {
          var coin = 'no price';
        } else {
          var coin = String(p[0]);
        }
        //console.log(coin);
        data[i].coin = coin;

        var price = low_price.split(/[^0-9]/);
        //console.log(price);
        for (var j = 0; j < price.length; j++) {
          //run on price array
          let priceNum = Number(price[j]);
          if (priceNum == 0) {
            price.splice(j, 1);
          }
        }

        if (price.length == 0) {
          var finalPrice = 'no price';
        } else {
          var finalPrice = Number(price[0]);
        }
        //console.log(finalPrice);
        data[i].price = finalPrice;
      }
      //console.log(data);
      //res.json(data);
      //return data;
    }, 500);
    //console.log(data);
    //return data;
  },

  measureMs: (operationName, time) => {
    const now = performance.now();
    const timeOfOperation = now - time;
    const durationParsed = millisToMinutesAndSeconds(timeOfOperation);
    console.log(
      `measureMs -> time for operation of "${
        operationName || 'operation'
      }": ${durationParsed}`,
    );
    console.log(time);
  },
};

function millisToMinutesAndSeconds(millis) {
  var minutes = Math.floor(millis / 60000);
  var seconds = ((millis % 60000) / 1000).toFixed(0);
  return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
}
