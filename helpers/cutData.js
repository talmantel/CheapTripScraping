const fs = require('fs');



const cutData = (fileString) => {
    let data = fs.readFileSync(`tables/${fileString}.html`, 'utf-8', (err) => {
        if (err) {
            console.error(err);
            return;
        }
    });

    // string operations
    let beginIndex = data.indexOf('<meta id=\'deeplinkTrip\' content=\'');
    beginIndex += 33; // shift to the end of 'indexOf' argument string
    let endIndex = data.indexOf('<meta id=\'deeplinkQuery\'');
    endIndex -= 6;
    data = data.slice(beginIndex, endIndex);
    fs.writeFileSync(`cut/${fileString}.json`, data, { flag: 'w+' });
};

module.exports = cutData;