const fs = require('fs');
const { performance } = require('perf_hooks');
const writeTable = require('./writeTable');
const https = require('https'); // native node.js module for HTTP requests
const measureMs = require('../performance_tests/measureMS');
const logMissedFile = require('../performance_tests/logMissedFile');

const grab = (params) => {
    const {from, from_id, to, to_id} = params;

      let t0 = performance.now();
	
      const options = {
      hostname: 'www.rome2rio.com',
      port: 443,
      path: `/map/${from}/${to}`,
      method: 'GET'
    };
    
    const req = https.request(options, res => {
      
      res.on('data', d => {
        fs.writeFileSync('tmp.txt', d, {flag: 'a'}); // data is fetched several times => need append flag
      });

      res.on('end', () => {
        console.log('Fetch data ok');
        
        let data = fs.readFileSync('tmp.txt', 'utf-8');
        data = data.toString();

        // clean everything we don't need in received HTML

        // Define text that will include JSON content only

        let substrBegin = '<meta id=\'deeplinkTrip\' content=\''; 
        let substrBeginIndex = data.indexOf(substrBegin);

        let substrEnd = '<meta id=\'deeplinkQuery\'';
        let substrEndIndex = data.indexOf(substrEnd);

        // Fine tune text grabbing indexes
        substrBeginIndex = substrBeginIndex + 33;
        
        substrEndIndex = substrEndIndex - 6;

        data = data.slice(substrBeginIndex, substrEndIndex);
        
        fs.writeFileSync(`tables/${from_id}_${from}_${to_id}_${to}.json`, data, {flag: 'w+'});
        // fs.writeFileSync(`tables/${from_id}_${from}_${to_id}_${to}.json`, from_id.toString(), {flag: 'w+'});

        // Important! We need to delete temporary file before new grabbing
        fs.rmSync('tmp.txt');
      });
    });

    // Connect to rom2rio + fetch HTML data + get JSON from meta tag + remove temporary file
    measureMs('single grab operation', t0);
    
    req.on('error', error => { 
      logMissedFile(error);
     });
    
    req.end();    
};

module.exports = grab;