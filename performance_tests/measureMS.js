// Modified Shulamit's func <3
const fs = require('fs');
const { performance } = require('perf_hooks');

const measureMs = (operationName, time) => {
  let memoryUsed = process.memoryUsage().heapUsed / 1024 / 1024;
  memoryUsed = Math.round(memoryUsed * 100) / 100;
  const now = performance.now();
  const timeOfOperation = now - time;
  const durationParsed = timeOfOperation;
  fs.writeFile(__dirname + '/performance.txt', `
    measureMs -> ${memoryUsed} MB is used at the moment\n
    measureMs -> time for operation of "${
    operationName || 'operation'
  }": ${durationParsed} ms\n`, { flag: 'a+' }, (e) => {
    console.log(e);
  });
}

module.exports = measureMs;
