// Simple travel. In the future we can expand this script

const csv = require('csv-parser');
const languageEncoding = require('detect-file-encoding-and-language');
const fs = require('fs');
const grabTrigger = require('./helpers/grabTrigger');

const pathToFile = process.argv[2];
const delay = parseInt(process.argv[3]) || 3000;

// TODO: обеспечить записи пар и проследить задержку
// grab({from: 'Saint%20Petersburg,Russia',from_id:33,to:'New%20York,USA',to_id:999})



if (!pathToFile || pathToFile.indexOf('.csv') === -1) {
  throw new Error('No CSV file provided!');
}

languageEncoding(pathToFile).then(fileInfo => {
  console.log(fileInfo.encoding);
  if (fileInfo.encoding !== 'UTF-8' || fileInfo.encoding !== 'utf-8'){
    //throw new Error('CSV encoding error, must be utf-8 w/o BOM');
  }
}).catch(error => console.log(error));

let cities1 = cities2 = []; // cities2 values will be copied to cities1
fs.createReadStream(pathToFile)
  .pipe(csv(['id', 'city', 'country']))
  .on('data', (data) => { 
    cities1.push([
      parseInt(data.id), 
      `${data.city.trim()},${data.country}`
    ]); 
    console.log(cities1.length, cities2.length);
  })
  .on('end', () => {
    
    // we should try/catch in all async/promise operations => no warnings from node
    try {
      grabTrigger(1, cities1, cities2);
      grabTrigger(0, cities1, cities2);

    } catch (error) {
      
      console.error(error);
    }
    
});
