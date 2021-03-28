const fs = require('fs');

let missedFiles = 0;

// get number of missed files and error why
const logMissedFile = (error) => {
    missedFiles++;
    fs.writeFileSync("missed.txt", `${missedFiles} - ${error}`, {"flag": "a+"});
}

module.exports = logMissedFile;