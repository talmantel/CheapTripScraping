const fs = require('fs');

const deleteFile = (filePath) => {
    // 'if' to avoid errors
    if (fs.existsSync(filePath)){
        fs.rmSync(filePath);
    }
}

module.exports = deleteFile;