var mqtt = require('mqtt');
var config = require('./config.json')

var client = mqtt.connect(config.server_url, {
  "username": "device",
  "password": config.device_id
})

var clientPrefix = '/' + config.client_id + '/';

client.on('connect', function () {
  client.publish(clientPrefix + 'device_info', JSON.stringify(config.device_info));
});

client.on(clientPrefix + '/buttons/1', function (topic, message) {
  console.log(message.toString());
});
