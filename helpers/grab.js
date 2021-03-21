const fs = require('fs');
const {promisify} = require('util');
const cheerio = require('cheerio');
const got = require('got');
const {CookieJar} = require('tough-cookie');
const writeToJSON = require('./writeToJSON');

const { performance } = require('perf_hooks');
const measureMs = require('../performance_tests/measureMS');
const writeTable = require('./writeTable');
const moveFile = require('./moveFile');



const grab = async (params) => { // {id, from, to, cookies}
   
    
    const {id, from, to} = params;

    const url = `https://www.rome2rio.com/map/${from}/${to}`;

    let t0 = performance.now();

    const cookieJar = new CookieJar();
    const setCookie = promisify(cookieJar.setCookie.bind(cookieJar));
    const cookies = setCookie('currency=EUR', url);
	
	await got(url, {cookies}).then(response => {
        
        const $ = cheerio.load(response.body);
        const result = $('#deeplinkTrip')[0].attribs.content;
        writeTable({id, from, to, data: result});
        moveFile(`${id}_${from}_${to}.json`, `tables/${id}_${from}_${to}.json`);
        
        //measureMs('fetch rom2rio', t0);
        return result;
      }).then((data) => {
            // JSON values need to be quoted
            let types = ['"flight"', '"ride"', '"plane"', '"bus"', '"ferry"'];
            //let t1 = performance.now();
            data = JSON.parse(data);
            data = data[2][1];
            
            // [0], [1], [2]
            // Grab transport type
            let transport = [];


                   
            
            let times = [22, 33];
            let prices = [10, 20, 30]; // todo

            // disabled now. uncomment to test
            //writeToJSON({ id, from, to, types, times, prices });
            
            //measureMs('make JSON file', t1);
    
      }).catch(err => {
        throw err;
      });

    
};

module.exports = grab;

