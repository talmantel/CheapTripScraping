const axios = require("axios");
const fixerAccessKey = '78e1130f74d936c6e569008d49179011';
const ratesURL = `http://data.fixer.io/api/latest?access_key=${fixerAccessKey}&format=1`;


const getRates = async () => {
    try {
        let response = await axios.get(ratesURL);
        let data = response.data;
        let rates = data.rates;
        //console.log(rates);
        return rates;
    } catch (error) {
        console.log(error);
    }
}

module.exports = getRates;