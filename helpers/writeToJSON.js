const fs = require('fs');

const writeToJSON = (params) => { // {id,from,to,info,times,prices}
    
    const {id,from,to,types,times,prices} = params;
    const filePath = `../results/${from}_${to}.json`;
    
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
    // TODO - fix ENOENT error
    if (!fs.existsSync(filePath)){
        fs.writeFile(filePath, content, (err) => {
            if (err) throw err;
        });
    } else {
        return;
    }

}

module.exports = writeToJSON;

  
