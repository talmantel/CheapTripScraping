const { performance } = require('perf_hooks');

module.exports = {
  measureMs: (operationName, time) => {
    const now = performance.now();
    const timeOfOperation = now - time;
    const durationParsed = millisToMinutesAndSeconds(timeOfOperation);
    console.log(
      `measureMs -> time for operation of "${
        operationName || 'operation'
      }": ${durationParsed}`,
    );
    console.log(time);
  }
};

function millisToMinutesAndSeconds(millis) {
  var minutes = Math.floor(millis / 60000);
  var seconds = ((millis % 60000) / 1000).toFixed(0);
  return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
}