// Shulamit
const fs = require('fs');
const { performance } = require('perf_hooks');

function millisToMinutesAndSeconds(millis) {
    var minutes = Math.floor(millis / 60000);
    var seconds = ((millis % 60000) / 1000).toFixed(0);
    return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
  }

module.exports = {
  measureMs: (operationName, time) => {
    const now = performance.now();
    const timeOfOperation = now - time;
    const durationParsed = millisToMinutesAndSeconds(timeOfOperation);
    fs.writeFile(__dirname + '/performance.txt', `measureMs -> time for operation of "${
      operationName || 'operation'
    }": ${durationParsed}\n`, { flag: 'a+' }, (e) => {
      console.log(e);
    });
  }
};

