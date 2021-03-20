const fs = require('fs');

const cheerio = require('cheerio');
const got = require('got');


const writeToJSON = require('./writeToJSON');

const { performance } = require('perf_hooks');
const measureMs = require('../performance_tests/measureMS');
const writeTable = require('./writeTable');

const grab = async (params) => { // {id, from, to, cookies}
    // todo: id ?
    
    const {id, from, to, cookies} = params;

    const url = `https://www.rome2rio.com/map/${from}/${to}`;
    //const cookieJar = new CookieJar();
	//const setCookie = promisify(cookieJar.setCookie.bind(cookieJar));

    let t0 = performance.now();
	
	await got(url, {cookies}).then(response => {
        const $ = cheerio.load(response.body);
        const result = $('#deeplinkTrip')[0].attribs.content;
        writeTable({id, data: result});
        // fs.writeFileSync(`../results/tmp.json`, data, {
        //     'flag': 'w+'
        // });
        //measureMs('fetch rom2rio', t0);
        return result;
      }).then((data) => {
       // console.log(data);
            let t1 = performance.now();
            //let data = fs.readFileSync('../results/tmp.json', 'utf-8');
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
        
            
            let types = ["a", "b"];
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

