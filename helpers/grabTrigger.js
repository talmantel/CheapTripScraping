const sleep = require('system-sleep');
const grab = require('./grab');
const DELAY = parseInt(process.argv[3]) || 5000;


const grabTrigger = (cities1, cities2) => {
  for (let i = 0; i < cities1.length; i++) {
    for (let j = 0; j < cities2.length; j++) {


      if (cities1.length && cities2.length && cities1[i][1] !== cities2[j][1] && j !== i) {
        grab({
          from: cities1[i][1],
          from_id: cities1[i][0],
          to: cities2[j][1],
          to_id: cities2[j][0]
        })
      };

      sleep(DELAY);

    }
  }
}

module.exports = grabTrigger;