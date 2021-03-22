const getRates = require('./getRates');



const convertToEur = (currentRate = "USD", value = 0) => {
   let rates = getRates();
  console.log(rates);
    if (rates){
        let rate = rates[currentRate];
        return Math.round(value / rate);
    } else {
        console.error('convert error');
    }
}

console.log(convertToEur("ILS", 77.6633));

module.exports = convertToEur;