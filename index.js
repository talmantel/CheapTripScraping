// Simple travel. In the future we can expand this script
const csv = require('csv-parser');
const {promisify} = require('util');
const fs = require('fs')
const {CookieJar} = require('tough-cookie');
const grab = require('./helpers/grab');

let cities1 = cities2 = [];
fs.createReadStream('cities.csv')
  .pipe(csv())
  .on('data', (data) => {
    cities1.push([data.id, `${data.city},${data.country}`])
    cities2.push([data.id, `${data.city},${data.country}`])
  })
  .on('end', () => {
      const cookieJar = new CookieJar();
      const setCookie = promisify(cookieJar.setCookie.bind(cookieJar));
      const cookies = setCookie('currency=EUR', 'https://www.rome2rio.com');
    
      for (let i = 0; i < cities1.length; i++){
        for (let j = 1; j < cities2.length - 1; j++){
          if (cities1[i] !== cities2[j]){
            grab({
              id: cities1[i].id,
              from: cities1[i].from,
              to: cities2[j].to,
              cookies
            });
          }
        }
      }

    
  });












