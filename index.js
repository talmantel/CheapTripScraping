// Simple travel. In the future we can expand this script

const csv = require('csv-parser');
const languageEncoding = require('detect-file-encoding-and-language');

const fs = require('fs');
const grabTrigger = require('./helpers/grabTrigger');

const pathToFile = process.argv[2];
const delay = parseInt(process.argv[3]) || 3000;


if (!pathToFile || pathToFile.indexOf('.csv') === -1) {
  console.error('No CSV file provided!');
  return;
}

languageEncoding(pathToFile).then(fileInfo => {
  let encoding = fileInfo.encoding.toString().toUpperCase();
  if (encoding !== 'UTF-8') {
    console.error('Wrong encoding! Must be UTF-8 w/o BOM');
    return;
  }
}).catch(error => {
  console.log(error);
  return;
});

let cities1 = cities2 = []; // cities2 values will be copied to cities1

fs.createReadStream(pathToFile)
  .pipe(csv(['id', 'city', 'country']))
  .on('data', (data) => {
    cities1.push([
      parseInt(data.id),
      `${data.city.trim()},${data.country}`
    ]);
    cities2.push([
      parseInt(data.id),
      `${data.city.trim()},${data.country}`
    ]);
  })
  .on('end', () => {
    // we should try/catch in all async/promise operations => no warnings from node
    try {
      console.log('csv done');
      grabTrigger(cities1, cities2);
    } catch (error) {
      console.error(error);
    }
  });
