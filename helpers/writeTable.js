
const fs = require('fs');

const writeTable = (params) => {
    const {id, from, to, data} = params;
    fs.mkdir(`tables/${from}`, { recursive: true }, (err) => {
        if (err) throw err;
      });
    
    fs.writeFileSync(`tables/${from}/${id}_${from}_${to}.json`, data, { flag: 'w+'});
    // fs.writeFileSync(`${id}.txt`, 
    //     `===========================================\n`, 
    // { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `${data}\n`, { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `\n`, { flag: 'a+'});

}

module.exports = writeTable;