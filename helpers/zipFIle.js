const admZip = require('adm-zip');
const zipper = new admZip();

const fs = require('fs');

const zipFile = (filePath) => {
    // 'if' to avoid errors
    if (fs.existsSync(filePath)){
        zipper.addLocalFile(filePath);  
        fs.writeFileSync(`tables/${filePath}.zip`, zipper.toBuffer());
    }  
}

module.exports = zipFile;