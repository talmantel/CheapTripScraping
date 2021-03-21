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

function checkForFile(fileName,callback) // deprecated!
{
    fs.exists(fileName, function (exists) {
        if(exists)
        {
            callback();
        }else
        {
            fs.writeFile(fileName, 'test', {flag: 'wx'}, function (err, data) 
            { 
                callback();
            })
        }
    });
}

module.exports = checkForFile;