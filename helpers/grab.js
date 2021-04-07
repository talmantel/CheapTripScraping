const fs = require('fs');
const { performance } = require('perf_hooks');
const https = require('https'); // native node.js module for HTTP requests
const measureMs = require('../performance_tests/measureMS');
const logError = require('../performance_tests/logError');
const cutData = require('./cutData');

let counter = 0;
let fileString = ''; // path + contents
const grab = (params) => {

  const { from, from_id, to, to_id } = params;

  counter++;
  if (from !== to && counter % 2 == 1) {// to prevent double connection

    let t0 = performance.now();
    const path = encodeURI(`map/${from}/${to}`)
    const url = `https://www.rome2rio.com/${path}`

    fileString = `${from_id},${from},${to_id},${to}`;
    try {

      https.get(url, (res) => {
        console.log(`counter: ${counter}, status code: ${res.statusCode}`);
        res.on('data', (d) => {
          // page is fetched several times (several chunks - that is why we need append flag)
          fs.writeFileSync(`tables/${fileString}.html`, d, { flag: 'a+' });
        });
        res.on('end', () => {
          try {
            cutData(fileString);
            measureMs('grab.js', t0, from, to);
          } catch (error) {
            logError(error);
            throw new Error(error);
          }
        });
      });
    } catch (error) {
      logError(error);
      throw new Error(error);
    }
  }
}


module.exports = grab;