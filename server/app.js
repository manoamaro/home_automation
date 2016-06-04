var firebase = require('firebase');
var firebaseConfig = require('./firebase.json');
var mosca = require('mosca');

var app = firebase.initializeApp(firebaseConfig);
var auth = firebase.auth();
var db = firebase.database();

var ref = db.ref("/devices");

var pubsubsettings = {
  type: 'redis',
  redis: require('redis'),
  db: 12,
  port: 6379,
  return_buffers: true,
  host: "localhost"
};

var moscaSettings = {
  port: 1883,
  backend: pubsubsettings
};

var server = new mosca.Server(moscaSettings);
server.on('ready', setup);

// Accepts the connection if the username and password are valid
var authenticate = function(client, username, password, callback) {
  if (username === 'device') {
    ref.orderByKey().equalTo(password).once("value", function(snap) {
      callback(null, true);
    });
  }
}

// In this case the client authorized as alice can publish to /users/alice taking
// the username from the topic and verifing it is the same of the authorized user
var authorizePublish = function(client, topic, payload, callback) {
  callback(null, client.user == topic.split('/')[1]);
}

// In this case the client authorized as alice can subscribe to /users/alice taking
// the username from the topic and verifing it is the same of the authorized user
var authorizeSubscribe = function(client, topic, callback) {
  callback(null, client.user == topic.split('/')[1]);
}

function setup() {
  server.authenticate = authenticate;
  server.authorizePublish = authorizePublish;
  server.authorizeSubscribe = authorizeSubscribe;
  console.log('Mosca server is up and running')
}

server.on('clientConnected', function(client) {
    console.log('client connected', client.id);
});

server.on('published', function(packet, client) {
  console.log('Published', packet.payload);
});
