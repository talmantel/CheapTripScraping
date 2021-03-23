const fs = require('fs');
const admZip = require('adm-zip');
const putToFile = require('./putToFile');

const zipper = new admZip();

const writeTable = (params) => {
    const {from, to, data} = params;
    
        

        fs.mkdir(`tables/${from}`, { recursive: true }, (err) => {
            if (err) throw err;
          });
        
        zipper.addLocalFolder(`tables/${from}`, `results/tables/${from}`);
    
        
    
        const filePath = `tables/${from}/${from}_${to}.json`;
        
        putToFile(filePath, data);
        
              
        zipper.addLocalFile(filePath, `results/${filePath}`);
    
        fs.writeFileSync('results.zip', zipper.toBuffer());


    
     
   // fs.writeFileSync(`tables/${from}/${id}_${from}_${to}.json`, data, { flag: 'w+'});
    // fs.writeFileSync(`${id}.txt`, 
    //     `===========================================\n`, 
    // { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `${data}\n`, { flag: 'a+'});
    // fs.writeFileSync(`${id}.txt`, `\n`, { flag: 'a+'});

}

module.exports = writeTable;