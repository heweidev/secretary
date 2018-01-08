var express = require('express');
var router = express.Router();
var dbHelper = require('../db');
var err = require('../common/error')
const url = require('url');
const {ObjectID} = require('mongodb');

router.get('/listNotes', function (req, res, next) {
    var query = url.parse(req.url, true).query;
    console.log(query);
    var queryParam = null;
    if (query.id) {
        queryParam = {_id: new ObjectID(query.id)};
    } else if (query.key) {
        queryParam = {title: {$regex: query.key}};
    }

    doSearch(queryParam).then(function(values){
        res.json(values);
    }).catch(function(err){
        res.json({err: err});
    });
});

function doSearch(queryParam) {
    const db = dbHelper.getDb();
    if (!db) {
        throw new Error('db not ready!');
    }

    const collection = db.collection('notes');
    const data = db.collection('data');

    var findRet;
    if (!queryParam) {
        findRet = collection.find();
    } else {
        findRet = collection.find(queryParam);
    }
    
    return findRet.toArray().then(
        function (notes) {
            return Promise.all(notes.map(function (note) {
                console.log('query ' + note._id);

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

router.post('/addNote', function(req, res, next) {
    const db = dbHelper.getDb();
    if (!db) {
        throw new Error('db not ready!');
    }

    const body = req.body;
    if (!body) {
        res.json(err.param_err('no data'));
        return;
    }
    if (!body.title || body.title.length == 0) {
        res.json(err.param_err('title can not be null'));
        return;
    }

    var note = {
        title: body.title,
        desc: body.desc,
        tags: body.tags,
        date: new Date().toUTCString(),
    }

    const colNotes = db.collection('notes');
    colNotes.insertOne(note, function (err, ret) {
        const colData = db.collection('data');
        // ToDo data check body.data
        var noteId = ret.insertedId;
        for (var d in body.data) {
            body.data[d].noteId = noteId; 
        }

        colData.insertMany(body.data, function(err, ret){
            res.json({
                id: noteId
            });
        }); 
    });
});

router.post('/addData', function(req, res, next) {
    var noteId = url.parse(req.url, true).query.id;

    const db = dbHelper.getDb();
    if (!db) {
        throw new Error('db not ready!');
    }

    if (noteId == '') {
        throw Error('id is null');
    }

    var itemData = req.body;
    itemData.noteId = new ObjectID(noteId);

    const data = db.collection('data');
    data.insertOne(itemData).then(function(doc){
        res.send(doc.insertedId);
    }).catch(function(err){
        res.send(err);
    });
});

/* GET users listing. */
router.use('/', function (req, res, next) {
    const db = dbHelper.getDb();
    if (!db) {
        throw new Error('db not ready!');
    }

    const collection = db.collection('notes');
    if (req.method == 'GET') {

        collection.find().toArray(function (err, docs) {
            if (err) throw err;
            res.json(docs);
        });
    } else if (req.method == 'POST') {
        const body = req.body;
        if (!body) {
            res.json(err.param_err('no data'));
            return;
        }
        if (!body.title || body.title.length == 0) {
            res.json(err.param_err('title can not be null'));
            return;
        }

        collection.insertOne(req.body, function (err, ret) {
            res.json({
                id: ret.insertedId
            });
        });
    } else if (req.method == 'PUT') {

    } else if (req.method == 'DELETE') {

    }
});

module.exports = router;