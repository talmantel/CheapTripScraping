const putToFile = require('./putToFile');
const zipFile = require('./zipFile');
const deleteFile = require('./deleteFile');

const delay = process.argv[3] || 3000;

const writeTable = (data, filePath) => {
    putToFile(filePath, data);
    setTimeout(() => zipFile(filePath), delay); // =sleep(ms)
    setTimeout(() => deleteFile(filePath), delay);
}

module.exports = writeTable;