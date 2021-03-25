const putToFile = require('./putToFile');
const zipFile = require('./zipFile');
const deleteFile = require('./deleteFile');

const delay = process.argv[3] || 3000;

const writeTable = (data, filePath) => {
    
    if(filePath) setTimeout(() => { // =sleep(ms)
        putToFile(filePath, data);
        zipFile(filePath), delay;
        deleteFile(filePath), delay;
    });
}
module.exports = writeTable;