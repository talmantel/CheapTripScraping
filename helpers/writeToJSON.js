const fs = require('fs');

const writeToJSON = (key, value, mode = "std", newLine = true) => {
    let content = '';
    if (mode === 'begin'){
        content = '{\n';
    } else if (mode === 'end'){
        content = '}\n';
    } else {
        content = `{${key}:${value}}${newLine ? ',\n' : ''}`
    }

    fs.writeFileSync("out.json", content, {"flag": "a+"});
}

module.exports = writeToJSON;

  
