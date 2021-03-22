// Simple travel. In the future we can expand this script
const csv = require('csv-parser');
const fs = require('fs');
const { performance } = require('perf_hooks');
const grab = require('./helpers/grab');


let cities1 = cities2 = [];
fs.createReadStream('India for scraping.csv')
  .pipe(csv())
  .on('data', (data) => { // -800 india
    cities1.push([parseInt(data.id)-800, `${data.city.trim()},${data.country}`]);
    //cities2.push([parseInt(data.id)-800, `${data.city},${data.country}`]);
  })
  .on('end', () => {
    const measureMS = require('./performance_tests/measureMS');
    let t0 = performance.now();
    console.log(cities1); //, cities2);
    let from = to = '';
    for (let i = 0; i < cities1.length; i += 2){
      for (let j = 1; j < cities2.length - 1; j += 2){
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












