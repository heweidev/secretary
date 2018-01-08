
const url = require('url')
const path = require('path');
const { URL, URLSearchParams } = require('url');

var user = new URL('http://localhost/user/1/2');
var user = url.parse('http://localhost/user/1/2', true);
console.log(user)
//console.log(user);

const myURL = new URL('https://example.org/?abc=123');
console.log(myURL.searchParams.get('abc'));




var array = [ 'a', 'b', 'c'];
for (i in array) {
    console.log(i, array[i])
}