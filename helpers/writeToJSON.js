const fs = require('fs');
const touch = require('touch'); // creates new empty file (forcing)
const putToFile = require('./putToFile');


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
                ${types.length ? types.join(',\n\t') : ''}
            ],
            "time": [
                ${times.length ? times.join(",\n\t") : ''}
            ],
            "price": [
                ${prices.length ? prices.join(",\n\t") : ''}
            ]
        }
    } 
    `;

    
    // TODO: Fix json data
    // check for file, if file exists -> write
    putToFile(filePath, content);
}




module.exports = writeToJSON;

  
