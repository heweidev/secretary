
const url = require('url')
const path = require('path');

var user = url.parse('http://localhost/user/1/2?key=1')
var p = path.parse(user.path);
console.log(p);
console.log(user)
//console.log(user);

user.query


var array = [ 'a', 'b', 'c'];
for (i in array) {
    console.log(i, array[i])
}