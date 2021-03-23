const fs = require('fs');
const {promisify} = require('util');
const cheerio = require('cheerio');
const got = require('got');
const {CookieJar} = require('tough-cookie');

const writeToJSON = require('./writeToJSON');
const { performance } = require('perf_hooks');

const writeTable = require('./writeTable');
const moveFile = require('./moveFile');
const convertToEur = require('../rates/convertToEur');

const grab = async (params) => { // {id, from, to, cookies}
   
    
    const {id, from, to} = params;

    const url = `https://www.rome2rio.com/map/${from}/${to}`;

    let t0 = performance.now();

    const cookieJar = new CookieJar();
    const setCookie = promisify(cookieJar.setCookie.bind(cookieJar));
    const cookies = setCookie('currency=EUR', url);
	
	await got(url, {cookies}).then(response => {
        try {
            const measureMs = require('../performance_tests/measureMS');
            let t1 = performance.now();
            let timeout = parseInt(process.argv[3]) || 2000;
          
            setTimeout(() => {
              const $ = cheerio.load(response.body);
              const result = $('#deeplinkTrip')[0].attribs.content;
  
              if (!result){
                console.error('Error retrieving connection');
                return;
              }
  
              
  
              writeTable({id, from, to, data: result});
              moveFile(`${id}_${from}_${to}.json`, `tables/${id}_${from}_${to}.json`);
            }, timeout);

            
            
            measureMs('fetch rom2rio', t1);
            return result;
        } catch (error) {
          console.log(error, 'grab.js got');
        }

      }).then((data) => {
          try {
            // disabled now. uncomment to test
            // JSON values need to be quoted
            //let types = ['"flight"', '"ride"', '"plane"', '"bus"', '"ferry"'];
            //let t1 = performance.now();
            //data = JSON.parse(data);
            //data = data[2][1];
            
            // [0], [1], [2]
            // Grab transport type
           /* let transport = [];
            let times = [22, 33];
            let prices = [10, 11, 12];*/
           // let prices = [10, 20, 30, convertToEur(55.141485)]; // todo

            
            //writeToJSON({ id, from, to, types, times, prices });
            
            //measureMs('make JSON file', t1);
          } catch (error) {
            console.log(error, 'grab.js then');
          }
          
    
      }).catch(err => {
        throw err;
      });

    
};

module.exports = grab;

