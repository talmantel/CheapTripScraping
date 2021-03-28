const grab = require('./grab');

const grabTrigger = (flag = 0, cities1, cities2) => {
    let from = to = '';
    if (flag == 0){ // pair of 2 values from csv
      for (let i = 1; i < cities1.length; i++){
        for (let j = 1; j < cities2.length; j++){
            grab({
              from: cities1[i][1],
              from_id: cities1[i][0],
              to: cities2[j][1],
              to_id: cities2[j][0]
          })
        }
      }
    } else { // flag == 1, pair vice versa
      for (let i = 1; i < cities1.length; i++){
        for (let j = 1; j < cities2.length; j++){
          if (cities1[i][0] !== cities2[j][0]){
            grab({
              from: cities1[cities1.length - i][1],
              from_id: cities1[cities1.length - i][0],
              to: cities2[cities2.length - j][1],
              to_id: cities2[cities2.length - j][0]
            })
          }
        }
      }
    }
  }

module.exports = grabTrigger;