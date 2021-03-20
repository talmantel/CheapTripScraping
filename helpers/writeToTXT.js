const fs = require('fs');

const writeToTXT = (data, newLine = true) => {
    fs.writeFileSync("out.txt", (newLine ? data : data + '\n')), {"flag": "w+"}
};


module.exports = writeToTXT;