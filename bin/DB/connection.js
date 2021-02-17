// require('dotenv').config();
var mysql = require('mysql');
var pool = null;

/* DB Connection Parameters */
const db_name = 'travel';
const db_user = 'root';
const db_pass = 'sh1591asdjkl';
//const db_pass = "";
const db_host = 'localhost';

// Initialize pool
var con = {
  getpool: function () {
    if (pool) {
      return pool;
    }
    pool = mysql.createPool({
      host: db_host,
      user: db_user,
      password: db_pass,
      database: db_name,
    });
    return pool;
  },
};

module.exports = con;