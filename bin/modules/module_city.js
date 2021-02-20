const converter = require('./csv-json-converter.js');




// Read data from CSV (replace hardcoded values that were before)

const cities = converter.convertCSVtoJSONarray('/data.csv');

  

var city_data = {
    createArrayId: function(){
        console.log(cities);
        return cities;
    }
}

module.exports = city_data;