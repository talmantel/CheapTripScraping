const throttledQueue = require('throttled-queue'); // TODO: try debug this *******
const fs = require('fs');
const { performance } = require('perf_hooks');
const https = require('https'); // native node.js module for HTTP requests
const writeTable = require('./writeTable');
const measureMs = require('../performance_tests/measureMS');
const logMissedFile = require('../performance_tests/logMissedFile');

let throttle = throttledQueue(5, 1000); // at most 5 requests per second.
let counter = 0;
const grab = (params) => {

  const { from, from_id, to, to_id } = params;

  if (from !== to) {
    counter++;

    let t0 = performance.now();
    // const path = encodeURI(`map/${from}/${to}`)
    // const url = `https://www.rome2rio.com/${path}`
    if (counter % 2 == 1) { // to prevent double connection
      measureMs('single grab operation', t0, from, to);
    }

    /* https.get(url, (res) => {
   //res.on('data', d => null);//buffer
   //res.on('end', () => fs.writeFileSync('tmp.csv', from_id + ',' + from + ',' + to_id + ',' + to + '\n', { flag: 'a+' }));
       //measureMs('single grab operation', t0, from, to);
   
       res.on('error', error => {
         logMissedFile(error);
       });
   
     })
   }*/
  }
}


module.exports = grab;