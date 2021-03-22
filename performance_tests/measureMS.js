// Shulamit's func :)
const fs = require('fs');
const { performance } = require('perf_hooks');

const measureMs = (operationName, time) => {
  const now = performance.now();
  const timeOfOperation = now - time;
  const durationParsed = timeOfOperation;
  fs.writeFile(__dirname + '/performance.txt', `measureMs -> time for operation of "${
    operationName || 'operation'
  }": ${durationParsed} ms\n`, { flag: 'a+' }, (e) => {
    console.log(e);
  });
}

module.exports = measureMs;

