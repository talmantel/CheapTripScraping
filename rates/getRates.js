const http = require('http');
const fixerAccessKey = '78e1130f74d936c6e569008d49179011';
const options = {
    hostname: 'data.fixer.io',
    port: 80,
    path: `/api/latest?access_key=${fixerAccessKey}&format=1`,
    method: 'GET'
};


const getRates = () => {
    try {
        const req = http.get(options);
        let rates = null;
      
        res.on('data', d => {
            rates = d.rates;
        });

        res.on('end', () => {
            return rates;
        });

        req.on('error', error => {
            console.error(error, 'getRates')
        });
          
        req.end();

        
    } catch (error) {
        console.log(error);
    }

    
}

module.exports = getRates;