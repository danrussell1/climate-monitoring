/**
 * Welcome to Pebble.js!
 *
 * This is where you write your app.
 */

var ajax = require('ajax');
var UI = require('ui');

ajax({url: 'http://98.226.222.143:8181/climate', type: 'json'},
function(data) {
  var content = 'Temperature: ' + data.temperature + ' °F\n' +
      'Humidity:\n' + data.humidity + '%\n' +
      'Heat Index:\n' + data.heatIndex + ' °F\n' +
      'Last Recorded:\n' + data.timestamp;
  
  var boomCard = new UI.Card({
    title: 'Office Data',
    body: content,  
    scrollable: true
  });
  boomCard.show();
   
},
function(error) {
console.log('Ajax failed: ' + error);
}
);