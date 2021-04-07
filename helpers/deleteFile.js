const fs = require('fs');

const deleteFile = (filePath) => {
    // 'if' to avoid errors
    if (fs.existsSync(filePath)) {
        fs.rmSync(filePath);
        return true;
    }
    return false;
}

module.exports = deleteFile;