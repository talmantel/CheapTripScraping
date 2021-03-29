const getRates = require('./getRates');



const convertToRate = (value = 0, targetRate = 'EUR') => {
  let rates = getRates();
  console.log(rates);
    if (rates){
        let rate = rates[targetRate];
        return Math.round(value / rate);
    } else {
        console.error('convert error');
    }
}


module.exports = convertToRate;