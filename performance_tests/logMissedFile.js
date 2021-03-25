const fs = require('fs');

let missedFiles = 0;

const logMissedFile = () => {
    missedFiles++;
    fs.writeFileSync("missed.txt", missedFiles, {"flag": "w+"});
}

module.exports = logMissedFile;