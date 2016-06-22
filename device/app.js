var mqtt = require('mqtt');
if (process.env['DEVICE'])
  var Gpio = require('onoff').Gpio;
else {
  var Gpio = function(arg0, arg1) {
    return {};
  }
 }
var config = require('./config.json')

var client = mqtt.connect(config.server_url, {
  "username": "device",
  "password": config.device_id
})

var clientPrefix = '/' + config.device_id + '/';
var buttons = {};

function onMessage(topic, message, packet) {
  var args = topic.split('/');
  if(args[1] == config.device_id) {
    if (args[2] == 'buttons' && buttons[args[3]]) {
      buttons[args[3]].writeSync(message.toString());
    }
  }
}

client.on('message', onMessage);

client.on('connect', function () {
  console.log('connected');
  client.publish(clientPrefix + 'device_info', JSON.stringify(config.device_info));

  for (var i = 0; i < config.device_info.buttons.length; i++) {
    buttons[config.device_info.buttons[i].id] = new Gpio(config.device_info.buttons[i].port, 'out');
    client.subscribe(clientPrefix + 'buttons/' + config.device_info.buttons[i].id);
  }

});
