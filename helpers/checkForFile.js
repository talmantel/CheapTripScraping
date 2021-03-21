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

function checkForFile(fileName, content, callback) // deprecated!
{
    fs.exists(fileName, function (exists) {
        if(exists)
        {
            return;
        }else
        {
            fs.writeFileSync(fileName, content, {flag: 'wx'});
        }
    });
}

module.exports = checkForFile;