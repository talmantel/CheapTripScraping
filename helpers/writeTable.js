const fs = require('fs');
const admZip = require('adm-zip');
const putToFile = require('./putToFile');

const zipper = new admZip();

const writeTable = (params) => {
    const {id, from, to, data} = params;
    
        

        fs.mkdir(`tables/${id}_${from}`, { recursive: true }, (err) => {
            if (err) throw err;
          });
        
        zipper.addLocalFolder(`tables/${id}_${from}`, `tables/${id}_${from}`);
    
        
    
        const filePath = `tables/${id}_${from}/${id}_${from}_${to}.json`;
        
        putToFile(filePath, data);
        
              
        zipper.addLocalFile(filePath, `${filePath}`);
    
        fs.writeFileSync('compressed.zip', zipper.toBuffer());


    
     
   // fs.writeFileSync(`tables/${from}/${id}_${from}_${to}.json`, data, { flag: 'w+'});
    // fs.writeFileSync(`${id}.txt`, 
    //     `===========================================\n`, 
    // { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `${data}\n`, { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `\n`, { flag: 'a+'});

}

module.exports = writeTable;