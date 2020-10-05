$(document).ready(function () {
  //cities_id();
    $('#Play').click(async () => {
        await cities_id();
    });
});

const port = 5000;
const url = 'http://localhost:' + port;
const id_transportation = [
  { type: 'fly', id: 1 },
  { type: 'bus', id: 2 },
  { type: 'train', id: 3 },
  { type: 'drive', id: 4 },
  { type: 'taxi', id: 5 },
  { type: 'walk', id: 6 },
  { type: 'towncar', id: 7 },
  { type: 'rideshare', id: 8 },
  { type: 'shuttle', id: 9 },
  { type: 'car ferry', id: 10 },
];
let id_cities = [];
let arrPlusLine = [];

async function cities_id() {
  try {
    const result = await axios.get(`${url}/getIdCities`);
    const { data } = result;

    if (!data) return;
    //console.log('functioncities_id -> data', data);
    id_cities = data;
    //console.log(id_cities);//list of id cities with names cities
    createArrayCities(id_cities);
    return id_cities;
  } catch (error) {
    console.log('functioncities_id -> error', error);
  }
}

function insertCityToDB(arrCities) {
  //console.log('insertCityToDB',arrCities);
  $.ajax({
    url: `http://localhost:${port}/insertCityToDB`,
    type: 'put',
    data: { arrCities },
    success: function (result) {
      console.log('success');
    },
    error: function (xhr) {
      console.log('Error:', xhr);
    },
  });
}

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function createArrayCities(id_cities) {
  try {
    if (!id_cities) return;

    let places = [];
    for (var i = 0; i < id_cities.length; i++) {
      var from = id_cities[i].city;
      for (var j = 0; j < id_cities.length; j++) {
        if (from != id_cities[j].city) {
          var to = id_cities[j].city;
          places.push({ from: from, to: to });
        }
      }
    }

    console.log({ places, length: places.length });

    for (var i = 0; i < places.length; i++) {
        try{
        const { from, to } = places[i];
        if (!from || !to) return;
        let operation = await search(from, to, id_cities);
        //let operation = await search(id_cities);
        //console.log(operation,"places[i]:",places[i]);
        //console.log("places[i]:",places[i]);
        if(!operation){
          console.error(
            `error on iteration: ${i} with ${JSON.stringify(places[i])}`,
          );
        }

        const { d_sorted, dataCityId } = operation;
        if (!d_sorted || !dataCityId) return;
        //console.log(d_sorted);
        let result_d_sorted = updateCityId(d_sorted, dataCityId);
        console.log(result_d_sorted);
        //let insert_result = await insertData(result_d_sorted);
        //console.log('createArrayCities -> insert_result', insert_result);
        sleep(100);
      }catch{
        console.log('createArrayCities -> insert_result -> error', error);
      }
    }
  } catch (error) {
    console.log('createArrayCities -> error', error);
  }
}

async function search(from, to, id_cities) {
   //async function search(id_cities) {  
   //let from = "London, England";
   //let to = "Slough, England";
  try {
    let request = await axios({
      method: 'post',
      url: url + '/getData',
      headers: {},
      data: {
        from,
        to,
        id_cities,
      },
    });

    if (!request) {
      console.error('search -> from, to, id_cities', from, to, id_cities);
      throw new Error('could not get response on search');
    }
    const { data: result } = request;
    //console.log("id_cities:",id_cities);//list of id cities with names cities
    var data = result[0];
    var from = result[1];
    var to = result[2];
    //var dataOnAllPrices = result[3]; 
    //console.log(dataOnAllPrices);
    var d_sorted = editTime(data);
    //await createTablePrices(from,to,dataOnAllPrices);
    return searchIdCities(from, to, d_sorted, id_cities);
    
  } catch (error) {
    console.log('search -> error', error);
  }
}

function editTime(data) {
  for (var i = 0; i < data.length; i++) {
    var time = data[i].time.split(/\s{1}/);
    var h_in_m = 0;
    var m = 0;
    var d_in_m = 0;
    for (var j = 0; j < time.length; j++) {
      var t = time[j];
      var num = time[1];
      if (num == "min"){
        //console.log(num,typeof num,time[0],typeof time[0]);
        m = parseInt(time[0]);
      }
      else{
          //console.log(t);
        if (t.includes('h')) {
          h_in_m = parseInt(t) * 60;
        }
        if (t.includes('m')) {
          //console.log(t);
          m = parseInt(t);
          //m = parseInt(time[j-1]);
        }
        if (t.includes('day')) {
          var days_time = time[j - 1] + t;
          var count_days = parseInt(days_time);
          d_in_m = count_days * 24 * 60;
        }
      }
      
    }
    var count_minutes = h_in_m + m + d_in_m;
    //console.log(count_minutes);
    data[i].time = count_minutes;
  }
  //console.log("func edit time:",data);
  if(!data[0].types){
    return data;
  }
  else{
    var data_sorted = sort(data);
    return data_sorted;
  }
  
}

function sort(data) {
  var arrData = data;
  var i = arrData.length;
  while (i--) {
    if (arrData[i].howMuchTypes > 1) {
      arrData.splice(i, 1);
    }
  }

  for (var j = 0; j < arrData.length; j++) {
    //console.log(arrData[j].types);
    let lines = (arrData[j].types).split(" ");
    //console.log(lines)
    lines.map((data)=> {
      if(data == 'line'|| data == 'Line'){
      //console.log(data,"true");
      dataDismantlement(arrData[j].types);
      }
    });
    
  }
  //console.log(arrPlusLine);
  var d = transportId(arrData, arrPlusLine);
  //console.log(d);
  return d;
}

function transportId(arr, arrPlus) {
  var arrData = arr;
  var arrDatalines = arrPlus;
  for (var i = 0; i < arrDatalines.length; i++) {
    //console.log(arrDatalines[i]);
    for (var j = 0; j < arrData.length; j++) {
      if (arrData[j].types.includes(arrDatalines[i].type)) {
        //console.log(arrData[j]);
        arrData[j].line = arrDatalines[i].line;
      }
    }
  }
  //console.log(arrData);
  for (var j = 0; j < arrData.length; j++) {
    if (arrData[j].types.includes('Fly') || arrData[j].types.includes('fly')) {
      arrData[j].types = 1;
    } else if (
      arrData[j].types.includes('Bus') ||
      arrData[j].types.includes('bus')
    ) {
      arrData[j].types = 2;
    } else if (
      arrData[j].types.includes('Train') ||
      arrData[j].types.includes('train')
    ) {
      arrData[j].types = 3;
    } else if (
      arrData[j].types.includes('Drive') ||
      arrData[j].types.includes('drive')
    ) {
      arrData[j].types = 4;
    } else if (
      arrData[j].types.includes('Taxi') ||
      arrData[j].types.includes('taxi')
    ) {
      arrData[j].types = 5;
    } else if (
      arrData[j].types.includes('Walk') ||
      arrData[j].types.includes('walk')
    ) {
      arrData[j].types = 6;
    } else if (
      arrData[j].types.includes('Towncar') ||
      arrData[j].types.includes('towncar')
    ) {
      arrData[j].types = 7;
    } else if (
      arrData[j].types.includes('Rideshare') ||
      arrData[j].types.includes('rideshare')
    ) {
      arrData[j].types = 8;
    } else if (
      arrData[j].types.includes('Shuttle') ||
      arrData[j].types.includes('shuttle')
    ) {
      arrData[j].types = 9;
    } else if(
      arrData[j].types.includes('Car ferry') ||
      arrData[j].types.includes('car ferry')
    ) {
      arrData[j].types = 10;
    }
  }
  //console.log(arrData);
  return arrData;
}

async function createTablePrices(from,to,prices){
  try{
    let lowPrice = 1000000;
    let lowPriceIndex = -1;
    //console.log(lowPrice);
    for(var j=0; j<prices.length; j++){
      let coin = prices[j].coin[0];
      //console.log(coin);
      prices[j].coin = coin;
      //console.log(prices[j].finalPrice,typeof prices[j].finalPrice,prices[j+1].finalPrice,typeof prices[j+1].finalPrice);
      if(prices[j].finalPrice){
        let priceNow = prices[j].finalPrice;
        if(priceNow<lowPrice){
          lowPrice = priceNow;
          lowPriceIndex = j;
        }
      } 
    }
    //console.log("check low price:",lowPrice,lowPriceIndex);
    for (var i=0; i<id_cities.length; i++){
      //console.log( "res:", res);
      if(from == id_cities[i].city){
        //console.log(id_cities[i]);
        from = id_cities[i].id;
      }
      if(to == id_cities[i].city){
        //console.log(id_cities[i]);
        to = id_cities[i].id;
      }
    }
    //console.log("from:",from,"to:",to,"low price:",lowPrice,"index in prices array:",lowPriceIndex);
    let dataLowPrice = [];
    dataLowPrice.push(prices[lowPriceIndex]);
    //console.log(dataLowPrice);
    var data = editTime(dataLowPrice);
    //console.log(data);
    //await insertLowPricesToDB(from,to,data);//travel controller!
  }catch(error) {
    console.log('createTablePrices -> error', error);
  }
}

function searchIdCities(from, to, d_sorted, id_cities) {
  var from_id;
  var to_id;
  //console.log(from, to, d_sorted,id_cities);
  if (!id_cities) return;

  var checkNamesFrom = [];
  var checkNamesTo = [];
  for (var i = 0; i < id_cities.length; i++) {
    var id = id_cities[i].city;
    //console.log(id);//its name city. not number!
    if (from.includes(id)) {  
      checkNamesFrom.push(id);
    }
    if (to.includes(id)) {  
      checkNamesTo.push(id);
    }
    
  }
  //console.log(checkNamesTo);
  //console.log(checkNamesFrom);
  if(checkNamesTo.length>1){
    let firstName = checkNamesTo[0];
    let long = firstName.length;
    let SecondlyName = checkNamesTo[1];
    let long2 = SecondlyName.length;
    if(long>long2){
      //to_id = id_cities[i].id;
      to_id = firstName;
    }
    else{
      to_id = SecondlyName;
    }
  }else{
    to_id = checkNamesTo[0];
  }

  if(checkNamesFrom.length>1){
    let firstName = checkNamesFrom[0];
    let long = firstName.length;
    let SecondlyName = checkNamesFrom[1];
    let long2 = SecondlyName.length;
    if(long>long2){
      from_id = firstName;
    }
    else{
      from_id = SecondlyName;
    }
  }else{
    from_id = checkNamesFrom[0];
  }
  
  for (var i = 0; i < id_cities.length; i++) {
    if(from_id == id_cities[i].city){
      from_id = id_cities[i].id;
    }
    if(to_id == id_cities[i].city){
      to_id = id_cities[i].id;
    }
  }
  //console.log(from_id,to_id);
  if (from_id == undefined || to_id == undefined) return null;
  var dataCityId = { from_id: from_id, to_id: to_id };
  //console.log(dataCityId, d_sorted);
  return { dataCityId, d_sorted };
  
}

// async function insertLowPricesToDB(from,to,prices){
//   //console.log(from,to,prices);
//   try {
//     let request = await axios({
//       method: 'put',
//       url: url + '/insertLowPricesToDB',
//       headers: {},
//       data: {
//         from,
//         to,
//         prices,
//       },
//     });

//     if (!request) {
//       console.error('createTablePrices -> insertLowPricesToDB -> from, to, prices', from, to, prices);
//       throw new Error('could not get response on search');
//     }
//     const { data: result } = request;
//     //console.log(result);
//     //console.log("id_cities:",id_cities);//list of id cities with names cities
//     //var data = result[0];
//     //var from = result[1];
//     //var to = result[2];
//     //var dataOnAllPrices = result[3]; 
//     //var d_sorted = sort(data);
//     //await createTablePrices(from,to,dataOnAllPrices);
//     //return searchIdCities(from, to, d_sorted, id_cities);
    
//   } catch (error) {
//     console.log('insertLowPricesToDB -> error', error);
//   }
// }

function updateCityId(d_sorted, dataCityId) {
  //console.log(d_sorted,dataCityId);
  for (var i = 0; i < d_sorted.length; i++) {
    d_sorted[i].from = dataCityId.from_id;
    d_sorted[i].to = dataCityId.to_id;
  }
  for (var j = 0; j < d_sorted.length; j++) {
    if (d_sorted[j].coin == '') {
      d_sorted.splice(j,1);
    }
  }
  return d_sorted;
}

function dataDismantlement(d_type) {
  //console.log(d_type);
  if(d_type.includes('line')){
    var newD = d_type.split(',');
    arrPlusLine.push({ line: newD[1] , type: newD[0] });
  }
  else{
    var newD = d_type.split(' ');
    arrPlusLine.push({ line: newD[0] + ' ' + newD[1], type: newD[2] });
  }
  //console.log(arrPlusLine);
  //console.log(newD);
  //arrPlusLine.push({ line: newD[0] + ' ' + newD[1], type: newD[2] });
}

// async function insertData(obj_data) {
//   //console.log(obj_data);
//   try {
//     var d = JSON.stringify(obj_data, null, 2);
//     let output = null;

//     await $.ajax({
//       url: `http://localhost:${port}/insert_Data`,
//       type: 'put',
//       data: { d },
//       success: function (result) {
//         console.log('success', result);
//         output = result;
//       },
//       error: function (xhr) {
//         console.log('Error:', xhr);
//       },
//     });
//     //console.log(output);
//     return output;
//   } catch (error) {
//     console.log('insertData -> error', error);
//   }
// }
