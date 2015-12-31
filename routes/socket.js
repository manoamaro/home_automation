module.exports = function(io) {
  var gpio = require('onoff').Gpio;
  
  var button = new gpio(16, 'in', 'both');
  var lights = {
    'l1': new gpio(17, 'out'),
    'l2': new gpio(27, 'out')
  };

  button.watch(function(err, value) {
    io.emit('update_temp', {'button': value})
  });
  
  io.on('connection', function(socket) {
    console.log('connected');
    socket.on('light', function(data) {
      console.log(data);
      lights[data['light']].write(data['value']);
    });
  });

}
