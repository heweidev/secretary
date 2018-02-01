var MongoClient = require('mongodb').MongoClient;
const assert = require('assert');

var url = "mongodb://localhost:27017/secretary";
const dbName = 'secretary';
var dbInst

/*
MongoClient.connect(url, function (err, client) {
    if (err) throw err;
    console.log("Connected successfully to server");
    dbInst = client.db(dbName);
});
*/

module.exports = {
    getDb: function() {
        return dbInst;
    }
}
