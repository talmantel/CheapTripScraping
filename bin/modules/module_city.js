const csv = require("csv-parser");
const fs = require("fs");


const cities = [];

// Read data from CSV (replace hardcoded values that were before)
// TODO: нужно чтобы содержимое файла было доступно вне фс

let content = fs.createReadStream('data.csv')
  .pipe(csv())
  .on('data', (row) => {
    cities.push(row);
  });

  

var city_data = {
    createArrayId: function(){
        console.log(cities);
        return cities;
    }
}

module.exports = city_data;