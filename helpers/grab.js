const cheerio = require('cheerio');
const got = require('got');
const { performance } = require('perf_hooks');

const writeTable = require('./writeTable');

const grab = async (params) => {
    const {from, from_id, to, to_id} = params;
    const url = `https://www.rome2rio.com/map/${from}/${to}`;
	
	await got(url).then(response => {
        try {
            const measureMs = require('../performance_tests/measureMS');
            let t1 = performance.now();
            const $ = cheerio.load(response.body);
            const result = $('#deeplinkTrip')[0].attribs.content;
  
            if (!result){
              console.error('Error retrieving connection');
              return;
            }
            measureMs('fetch rom2rio', t1);
  
            const fileName = `${from_id}_${from}_${to_id}_${to}.json`;
            let t2 = performance.now();
            writeTable(result, fileName);
            measureMs('write and zip', t2);
 
            return result;
        } catch (error) {
          console.log(error, 'grabTest.js got');
        }

      }).catch(err => { throw err; });

    
};

module.exports = grab;