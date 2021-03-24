// https://stackoverflow.com/questions/12899061/creating-a-file-only-if-it-doesnt-exist-in-node-js
// checks if the file exists. 
// If it does, it just calls back.
// If it doesn't, then the file is created.
const fs = require('fs');

// const checkForFile = (fileName,callback = null) => {
//     fs.access(fileName, fs.constants.F_OK, (err) => {
//         return !err;
//     });
// }

function putToFile(fileName, content, callback) // deprecated!
{
    // check if exists
    fs.exists(fileName, (exists) => {
        
        try {
            fs.writeFileSync(fileName, content, {flag: 'w+'});
            if (exists) return;
        
        } catch (error) {
            console.log(error, 'putToFile.js');
        }

    });
}

module.exports = putToFile; 