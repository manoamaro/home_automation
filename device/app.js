var mqtt = require('mqtt');
var Gpio = require('onoff').Gpio;
var config = require('./config.json')

var client = mqtt.connect(config.server_url, {
  "username": "device",
  "password": config.device_id
})

var clientPrefix = '/' + config.device_id + '/';
var buttons = {};

function onButtonPressed(topic, message) {

}

client.on('connect', function () {
  console.log('connected');
  client.publish(clientPrefix + 'device_info', JSON.stringify(config.device_info));

  for (var i = 0; i < config.device_info.buttons.length; i++) {
    buttons[config.device_info.buttons[i].id] = new Gpio(config.device_info.buttons[i].port, 'out');
    client.on(clientPrefix + 'buttons/' + config.device_info.buttons[i].id, onButtonPressed);
  }

});
