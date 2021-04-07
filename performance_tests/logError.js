const fs = require('fs');

let errorHits = 0;

// get number of missed files and error why
const logError = (error) => {
    errorHits++;

    fs.writeFileSync(__dirname + "/missed.txt", `${errorHits} file(s) with an error: ${error.message}\n`, { "flag": "a+" });

}

module.exports = logError;