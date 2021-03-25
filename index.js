// Simple travel. In the future we can expand this script
// TODO: fix zip problem
const csv = require('csv-parser');
const languageEncoding = require('detect-file-encoding-and-language');
const fs = require('fs');
const grab = require('./helpers/grab');


const pathToFile = process.argv[2];

if (!pathToFile || pathToFile.indexOf('.csv') === -1) {
  throw new Error('No csv file provided!');
}

languageEncoding(pathToFile).then(fileInfo => {
  if (fileInfo.encoding !== 'utf-8'){
    throw new Error('CSV encoding error, must be utf-8 w/o BOM');
  }
});

let cities1 = cities2 = []; // cities2 values will be copied to cities1
fs.createReadStream(process.argv[2])
  .pipe(csv(['id', 'city', 'country']))
  .on('data', (data) => { 
    cities1.push([parseInt(data.id), `${data.city.trim()},${data.country}`]); 
  })
  .on('end', () => {
    // we should try/catch in all async/promise operations => no warnings from node
    try {
      let from = to = '';
      for (let i = 1; i < cities1.length; i++){
        for (let j = 1; j < cities2.length; j++){
          if (cities1[i][0] !== cities2[j][0]){
            grab({
              from: cities1[i][1],
              from_id: cities1[i][0],
              to: cities2[j][1],
              to_id: cities2[j][0]
            })
          }
        }
      }
    } catch (error) {
      
      console.error(error);
    }

    
  });












