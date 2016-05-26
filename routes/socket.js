module.exports = function(io) {
  var nano = require('nano')('http://localhost:5984');
  var sensors = nano.db.use('sensors');
  var Gpio = require('onoff').Gpio;
  var TempLib = require('node-dht-sensor');
  var piblaster = require('pi-blaster.js');

  var devices = [
    {id: 'sw01', name: 'led01', type: 'switch'},
    {id: 'btn01', name: 'button01', type: 'button'},
    {id: 'sensor01', name: 'Temperature/Humidity', type: 'sensor'} 
  ];

  var gpios = {
    'sw01': new Gpio(26, 'out'),
    'btn01': new Gpio(24, 'in', 'both'),
    'sensor01': {port: 25, type: 22, lib: 'dht'}, 
  };

  gpios['btn01'].watch(function(err, value) {
    io.emit('update', {button: 'btn01', value: value});
  });
  
  function readSensorData(sensorId, type, port) {
    if (gpios[sensorId].lib == 'dht') {
      var data = TempLib.readSpec(type, port);
      data.temperature = Math.round10(data.temperature, -2);
      data.humidity = Math.round10(data.humidity, -2);
      return {
        id: sensorId,
        timestamp: new Date().getTime(),
        data: data
      };
    }
  }

  function startSensor(sensorId, type, port) {
    setInterval(function() {
      var sensorData = readSensorData(sensorId, type, port);
      sensors.insert(sensorData, function(err, body) {
        console.log(err);
        io.emit('update_sensor', sensorData);
        //humidity green
        piblaster.setPwm(17, (Math.abs((50 - sensorData.data.humidity) / 50) * -1) + 1);
        //humidity red
        piblaster.setPwm(4, Math.abs((sensorData.data.humidity - 50) / 50));

        //Temperature red
        piblaster.setPwm(21, Math.abs((sensorData.data.temperature - 26) / 12));
        //Temperature green
        piblaster.setPwm(22, (Math.abs((26 - sensorData.data.temperature) / 12) * -1) + 1);
      });
    }, 60000);
  }


  for (var i = 0; i < devices.length; i++) {
    if (devices[i].type == 'sensor') {
      var sensor = gpios[devices[i].id];
      if (sensor != null) {
        console.log('initializing sensor ' + devices[i].id);
        startSensor(devices[i].id, sensor.type, sensor.port);
      }
    }
  }




  io.on('connection', function(socket) {
    socket.emit('devices', devices);
    socket.emit('update_sensor', readSensorData('sensor01', 22, 25));

    socket.on('switch', function(data) {
      gpios[data['id']].writeSync(data['value']);
    });
  });
}
