const fs = require('fs');

const moveFile = (file, destination) => {
    fs.copyFile(file, destination, () => {
        if (fs.existsSync(file)){
            fs.rmSync(file);
        }
    });
}

module.exports = moveFile;