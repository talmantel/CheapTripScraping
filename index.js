// Simple travel. In the future we can expand this script
const csv = require('csv-parser')
const fs = require('fs');

const grab = require('./helpers/grab');

let cities = [];
fs.createReadStream('cities.csv')
  .pipe(csv())
  .on('data', (data) => cities.push(data.city))
  .on('end', () => {
    /*
      for (let i = 0; i < cities.length; i++){
        for (let j = 0; j < cities.length; j++){
          grab(cities[i][j], cities[i][j]);
        }
      }

    */
  });












