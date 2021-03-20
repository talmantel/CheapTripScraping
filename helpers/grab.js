const {promisify} = require('util');
const cheerio = require('cheerio');
const got = require('got');
const {CookieJar} = require('tough-cookie');

const writeToTXT = require('./writeToTXT');
const writeToJSON = require('./writeToJSON');

const { performance } = require('perf_hooks');
const measureMs = require('../performance_tests/measureMS');

const grab = async (from, to) => {
    const url = `https://www.rome2rio.com/map/${from.replace(' ', '-')}/${to.replace(' ', '-')}`;
    const cookieJar = new CookieJar();
	const setCookie = promisify(cookieJar.setCookie.bind(cookieJar));

    let t0 = performance.now();
	await setCookie('currency=EUR', url);
	await got(url, {cookieJar}).then(response => {
        const $ = cheerio.load(response.body);
        let data = $('#deeplinkTrip')[0].attribs.content;
        fs.writeFileSync('data.json', data, {
            'flag': 'w+'
        })
      }).catch(err => {
        console.log(err);
      });
    await measureMs('fetch rom2rio', t0);

    let t1 = performance.now();
    let data = fs.readFileSync('data.json', 'utf-8');
    data = JSON.parse(data);
    data = data[2][1];
    //console.log(Array.isArray(data));
    // [0], [1], [2]
    // Grab transport type
    let transport = [];

    for (let i = 0; i < 3; i++){
    for (let j = 0; j < data[i].length; j++){
        if (!Array.isArray(data[i][j]) && 
        (data[i][j] === 'Train' || data[i][j] === 'Bus' || data[i][j] === 'Car')){
        transport.push(data[i][j]);
        break;
        }
    }
    }

    writeToTXT(transport.join(' '));

    let prices = [10, 20, 30]; // todo
    writeToTXT(prices.join(' '));

    let currency = ['EUR', 'EUR', 'EUR'];
    writeToTXT(currency.join(' '));
    measureMs('make JSON file', t1);
};

module.exports = grab;

