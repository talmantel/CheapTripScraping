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
        fs.writeFileSync(fileName, content, {flag: 'a+'});
        
        // TODO: if file is not empty - return
    });
}

module.exports = putToFile;