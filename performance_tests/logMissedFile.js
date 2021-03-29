const fs = require('fs');

let missedFiles = 0;

// get number of missed files and error why
const logMissedFile = (error) => {
    missedFiles++;
    let fd = fs.openSync(__dirname + '/missed.txt');
    fs.writeFileSync(__dirname + "missed.txt", `${missedFiles} - ${error.message}`, {"flag": "a+"});
    fs.closeSync(fs);
}

module.exports = logMissedFile;