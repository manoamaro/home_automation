var mqtt = require('mqtt');
var config = require('./config.json')

var client = mqtt.connect(config.server_url)

client.on('connect', function () {
  client.publish('device_info', JSON.stringify(config.device_info));
});

client.on('message', function (topic, message) {
  console.log(message.toString());
});
