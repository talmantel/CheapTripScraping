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
        writeTable({id, data: result});
        moveFile(`${id}.txt`, `tables/${id}.txt`);
        
        //measureMs('fetch rom2rio', t0);
        return result;
      }).then((data) => {
            let types = ["flight", "ride", "plane", "bus", "ferry"];
            let t1 = performance.now();
            data = JSON.parse(data);
            data = data[2][1];
            
            // [0], [1], [2]
            // Grab transport type
            let transport = [];
        
            // for (let i = 0; i < 3; i++){
            // for (let j = 0; j < data[i].length; j++){
            //     if (!Array.isArray(data[i][j]) && 
            //     (data[i][j] === 'Train' || data[i][j] === 'Bus' || data[i][j] === 'Car')){
            //     transport.push(data[i][j]);
            //     break;
            //     }
            // }
            // }
        
            
            
            let times = [22, 33];
            let prices = [10, 20, 30]; // todo

            writeToJSON({ id, from, to, types, times, prices });
            
            //measureMs('make JSON file', t1);
            // как передать id
      }).catch(err => {
        console.log(err);
      });

    
};

module.exports = grab;

