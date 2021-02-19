var express = require('express');
var router = express.Router();

const usersRoutes = require('./users');
const mainRoutes = require('./mainRoute');

//all other code should be here
// users
router.use('/users', usersRoutes);
// mainRoutes
router.use('/', mainRoutes);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
