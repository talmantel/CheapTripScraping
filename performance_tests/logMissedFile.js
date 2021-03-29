const fs = require('fs');

let missedFiles = 0;

// get number of missed files and error why
const logMissedFile = (error) => {
    missedFiles++;
    
    fs.writeFileSync(__dirname + "/missed.txt", `${missedFiles} - ${error.message}\n`, {"flag": "a+"});
   
}

module.exports = logMissedFile;