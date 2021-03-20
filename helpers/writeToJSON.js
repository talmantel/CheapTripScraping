const fs = require('fs');

const writeToJSON = (params) => { // {id,from,to,info,times,prices}
    
    const {id,from,to,types,times,prices} = params;
    const filePath = `../results/${from}_${to}.json`;
    fs.writeFileSync(filePath, '{\n', {flag: "a+"});
    fs.writeFileSync(filePath, `\t\"id\": ${id}\n`, {flag: "a+"});
    fs.writeFileSync(filePath, `\t\"from\": ${from}\n`, {flag: "a+"});
    fs.writeFileSync(filePath, `\t\"to\": ${to}\n`, {flag: "a+"});
    fs.writeFileSync(filePath, `\t\"info\" {\n\"types\": [${types.length ? types.join(",\n") : ''}],`, {flag: "a+"});
    fs.writeFileSync(filePath, `\t{\n\"time\": [${times.length ? times.join(",\n") : ''}],`, {flag: "a+"});
    fs.writeFileSync(filePath, `\t{\n\"price\": [${prices.length ? prices.join(",\n") : ''}]\n\t}\n}`, {flag: "a+"});

}

module.exports = writeToJSON;

  
