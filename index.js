// Simple travel. In the future we can expand this script
const csv = require('csv-parser');
const fs = require('fs')
const grab = require('./helpers/grab');
const measureMS = require('./performance_tests/measureMS');

let cities1 = cities2 = [];
fs.createReadStream('India for scraping.csv')
  .pipe(csv())
  .on('data', (data) => {
    cities1.push([data.id, `${data.city},${data.country}`])
    cities2.push([data.id, `${data.city},${data.country}`])
  })
  .on('end', () => {
    let t0 = performance.now();
      
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

    measureMS('grab-jsons', t0);

    
  });












