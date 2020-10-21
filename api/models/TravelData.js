const sql = require('../DB/connection');
const { ErrorHandler } = require('../modules/error');

class TravelData {
  static async create(newEntry) {
    try {
      let result = await sql
        .promise()///<--not understand!!!
        .query('INSERT INTO `travel`.`travel_data` SET ?', newEntry);
      console.log('TravelData -> create -> result', result);
      //throw new ErrorHandler(400, 'missing origin and destination info');
      return result[0];
    } catch (error) {
      console.log('TravelData -> create -> error', error);
      return { error };
    }
  }
  static async getAll() {
    try {
      let result = await sql
        .promise()///<--not understand!!!
        .query('SELECT * FROM `travel`.`travel_data`');
      console.log('TravelData -> create -> result', result[0]);
      return result[0];
      //throw new ErrorHandler(400, 'missing origin and destination info');
    } catch (error) {
      console.log('TravelData -> create -> error', error);
      return { error };
    }
  }
}

module.exports = TravelData;