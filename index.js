// Simple travel. In the future we can expand this script
const csv = require('csv-parser');

const fs = require('fs')

const grab = require('./helpers/grab');

let cities1 = cities2 = [];
fs.createReadStream('cities.csv')
  .pipe(csv())
  .on('data', (data) => {
    cities1.push([data.id, `${data.city},${data.country}`])
    cities2.push([data.id, `${data.city},${data.country}`])
  })
  .on('end', () => {
      
      let from = to = '';
      for (let i = 0; i < cities1.length; i++){
        for (let j = 1; j < cities2.length - 1; j++){
          if (cities1[i] !== cities2[j]){
            grab({
              id: cities1[i][0],
              from: cities1[i][1],
              to: cities2[j][1]
            });
          }
        }
      }

    
  });












