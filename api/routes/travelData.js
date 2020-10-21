const express = require('express');
const router = express.Router();
const { TravelController } = require('../controllers/travelController');

router.get('/getIdCities', TravelController.getIdCities);

router.put('/insert_Data', function (req, res, next) {
  res.json({ route: 'insert_Data' });
});

router.get('/getAll', TravelController.getAll);

router.get('/getData', TravelController.getData);

module.exports = router;