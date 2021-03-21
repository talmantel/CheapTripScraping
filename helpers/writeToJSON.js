const fs = require('fs');
const touch = require('touch'); // creates new empty file (forcing)
const checkForFile = require('./checkForFile');


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
    
    // TODO: Fix json data
    // TODO: eur

    checkForFile(filePath, () => { // if file exists -> call callback writing func
       fs.writeFile(filePath, content, (err,data) => {
            throw new Error(`Fatal: writing to JSON failed: ${err}`);
       });
    });
}




module.exports = writeToJSON;

  
