var express = require('express');
var router = express.Router();
const travelRoutes = require('./travelData');
const usersRoutes = require('./users');
const mainRoutes = require('./mainRoute');

//all other code should be here
// users
router.use('/users', usersRoutes);
// mainRoutes
router.use('/', mainRoutes);
//travel
router.use('/travel', travelRoutes);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
