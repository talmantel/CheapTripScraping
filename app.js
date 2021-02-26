var createError = require('http-errors');
var express = require('express');
const methodOverride = require('method-override');
const ErrorHandler = require('./bin/modules/error');
var path = require('path');
var cors = require('cors');

const PORT = process.env.PORT || 5000;

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(cors());
app.use(methodOverride());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

var listener = app.listen(PORT, function(){
  console.log('Listening on port ' + listener.address().port);
  
});



app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
//app.use(ErrorHandler.handleError());

app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  //res.render('error');
});

module.exports = app;
