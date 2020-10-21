function parseTimeStr(timeStr) {
    // timeStr= '24h 9m'
  
    let resultStr = timeStr.split(' ');
    if (resultStr[1] == 'day' || resultStr[1] == 'days') {
      resultStr[0] = resultStr[0] + resultStr[1];
      resultStr.splice(1, 1);
    }
  
    const letters = /[A-Za-z]/g;
    const onlyNumRegex = /[0-9]/g;
  
    // resultStr = ['3day','24h','9m']
  
    resultStr = resultStr.map((subStr) => {
      let num = subStr.match(onlyNumRegex).join('');
      const word = subStr.match(letters).join('');
      num = parseInt(num);
      let num_output = 0;
  
      if (word == 'day' || word == 'days') {
        num_output = num * 24 * 60;
      }
  
      if (word == 'h') {
        num_output = num * 60;
      }
  
      return parseInt(num_output);
    });
  
    let minuteTotal = 0;
    resultStr.forEach((num) => {
      minuteTotal += num;
    });
  
    return minuteTotal;
  }
  
  module.exports = { parseTimeStr };