const {MongoClient, ObjectID} = require('mongodb');
const assert = require('assert');

var url = "mongodb://localhost:27017/secretary";
const dbName = 'secretary';

MongoClient.connect(url, function (err, client) {
    if (err) throw err;
    console.log("Connected successfully to server");

    const db = client.db(dbName);

    /*
    insertDocuments(db, function () {
        client.close();
    });
    */

    const collection = db.collection('notes');
    const data = db.collection('data');

    // Find some documents
    /*
    collection.find().toArray().then(
        function (docs) {
            return Promise.all(docs.map(function (item) {
                return data.find({
                        noteId: new ObjectID(item._id)
                    }).toArray().then(function (itemList) {
                        item.data = itemList;
                        return Promise.resolve(item);
                    });
            }));
        }).then(function(values){
            console.log(values);
        });
        */
        
        /*
    data.find({noteId: new ObjectID('5a4f1e554e5f2f2c54d3a66e')}).toArray().then(
        function(items){
            console.log(items);
        });*/
        
        /*
        const docs = [{id: '5a4f1e554e5f2f2c54d3a66e'}, {id:'5a4f1e554e5f2f2c54d3a66e'}];
        Promise.all(docs.map(function (item) {
            return data.find({
                    noteId: new ObjectID(item.id)
                }).toArray().then(function (itemList) {
                    console.log(itemList);
                    item.data = itemList;

                    return Promise.resolve(item);
                });
        })).then(function(values) {
            console.log(values);
        });*/

        /*
        doSearch(db, '2').then(function(values){
            console.log(values);
        }).catch(function(err){
            res.json({err: err});
        });
        */

        console.log(data.insertOne({a: 1}));

        
});

function doSearch(db, searchKey) {
    const collection = db.collection('notes');
    const data = db.collection('data');

    var findRet;
    if (searchKey == '') {
        findRet = collection.find();
    } else {
        findRet = collection.find({title: {$regex: searchKey}});
    }
    
    return findRet.toArray().then(
        function (notes) {
            return Promise.all(notes.map(function (note) {
                return data.find({
                        noteId: new ObjectID(note._id)
                    }).toArray().then(function (itemList) {
                        console.log('query ' + note._id + ' finished!');
                        note.data = itemList;
                        return Promise.resolve(note);
                    });
            }));
        });
}