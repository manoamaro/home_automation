var firebase = require('firebase');
var firebaseConfig = require('./firebase.json');

var app = firebase.initializeApp(firebaseConfig);

var auth = firebase.auth();
var idToken = 'a6rsaiYohnOCw1PPVC93gwG70wG2';  // Get the user's ID token from the client app

auth.verifyIdToken(idToken).then(function(decodedToken) {
  var uid = decodedToken.sub;
});
