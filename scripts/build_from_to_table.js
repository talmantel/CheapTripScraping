const sql = require('../api/DB');
const cities = require('../api/modules/module_city').createArrayId();
console.log('cities', cities[0]);

const fromToDbBuilder = async () => {
  try {
    const cityArrLength = cities.length;
    for (let i = 0; i < cityArrLength; i++) {
      const fromCityId = cities[i].id;
      for (let j = 0; j < cityArrLength; j++) {
        const toCityId = cities[j].id;
        if (fromCityId != toCityId) {
          const store_result = await storeFromTo(fromCityId, toCityId);
          console.log('fromToDbBuilder -> store_result', i, store_result);
          if (!store_result) throw new Error('error at iteration' + i, j);
        }
      }
    }
  } catch (error) {
    console.log('fromToDbBuilder -> error', error);
    // process.exit(1);
  }
};

const storeFromTo = async (from, to) => {
  try {
    console.log('storeFromTo -> { from, to }', { from, to });
    let result = await sql
      .promise()
      .query('INSERT INTO `travel`.`from_to` SET ?', { from, to });
    return result[0];
  } catch (error) {
    console.log('TravelData -> create -> error', error);
    // return { error };
    // process.exit(1);
  }
};

fromToDbBuilder();

// storeFromTo(100, 101);