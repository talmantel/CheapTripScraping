const { performance } = require('perf_hooks');

module.exports = {
  measureMs: (operationName, start_time_ms) => {
    const now = performance.now();
    const timeOfOperation = now - start_time_ms;
    const durationParsed = millisToMinutesAndSeconds(timeOfOperation);
    console.info(
      `measureMs -> time for operation of "${
        operationName || 'operation'
      }": ${durationParsed}`,
    );
    return durationParsed;
  },
};

function millisToMinutesAndSeconds(millis) {
  var minutes = Math.floor(millis / 60000);
  var seconds = ((millis % 60000) / 1000).toFixed(0);
  return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
}