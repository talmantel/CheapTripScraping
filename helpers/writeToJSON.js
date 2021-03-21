const fs = require('fs');
const checkForFile = require('./checkForFile');
const touch = require('touch'); // creates new empty file (forcing)

const writeToJSON = (params) => { // {id,from,to,info,times,prices}
    
    const {id,from,to,types,times,prices} = params;
    fs.mkdir(`results/${from}`, { recursive: true }, (err) => {
        if (err) throw err;
      });
    const filePath = `results/${from}/${from}_${to}.json`;
    
    let content = `
    {
        "id": ${id},
        "from":"${from}",
        "to":"${to}",
        "info":{
            "types": [
                ${types.length ? types.join(',\n') : ''}
            ],
            "time": [
                ${times.length ? times.join(",\n") : ''}
            ],
            "price": [
                ${prices.length ? prices.join(",\n") : ''}
            ]
        }
    } 
    `;
    // TODO: Error: EPERM: operation not permitted, futime - fix!
    // TODO: Fix json data
    // TODO: eur

    touch.sync(filePath, {force: true})
        .then(() => {
            console.log('touch ok')
            fs.writeFile(filePath, content, {flag: 'wx'}, (err) => {
                if (err) throw err;
            });
        })
        .catch(e => console.log(e));

    /*
    if (checkForFile(filePath)){
        console.log(checkForFile(filePath));
        fs.writeFile(filePath, content, {flag: 'wx'}, (err) => {
            if (err) throw err;
        });
    } else {
        return;
    }*/

}

module.exports = writeToJSON;

  
