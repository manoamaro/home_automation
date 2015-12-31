
module.exports = function(io) {
  var express = require('express');
  var router = express.Router();
  var gpio = require('onoff').Gpio;
  
  var button = new gpio(16, 'in', 'both');
  var lights = {
    'l1': new gpio(17, 'out'),
    'l2': new gpio(27, 'out')
  };

  button.watch(function(err, value) {
    io.emit('update_temp', {'button': value})
  });
  
  /* GET home page. */
  router.get('/', function(req, res, next) {
    res.render('index', { title: 'Express' });
  });

  io.on('connection', function(socket) {
    console.log('connected');
    socket.on('light', function(data) {
      console.log(data);
      lights[data['light']].write(data['value']);
    });
  });


  return router;
}
