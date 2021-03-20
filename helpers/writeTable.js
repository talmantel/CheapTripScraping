// id + rome2rio result

const fs = require('fs');

const writeTable = (params) => {
    const {id, data} = params;
    
    fs.writeFileSync(`${id}.txt`, `${id}\n`, { flag: 'a+'});
    fs.writeFileSync(`${id}.txt`, 
        `===========================================\n`, 
    { flag: 'a+'});
    fs.writeFileSync(`${id}.txt`, `${data}\n`, { flag: 'a+'});
    fs.writeFileSync(`${id}.txt`, `\n`, { flag: 'a+'});

}

module.exports = writeTable;