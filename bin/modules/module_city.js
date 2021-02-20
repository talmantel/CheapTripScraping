const converter = require('./csv-json-converter.js');




// Read data from CSV (replace hardcoded values that were before)

const cities = converter.convertCSVtoJSONarray('/data.csv');

  
module.exports = {
  createArrayId: function(){
      return cities;
  }
}
