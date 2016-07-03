/**
 * Created by Francis on 03/07/2016.
 */

var MongoClient = require('mongodb').MongoClient
    , assert = require('assert');

var url = 'mongodb://localhost:27017/tyche';
MongoClient.connect(url, function(err, db) {
    db.drop();

    var killmails = db.collection("killmails");
    killmails.createIndex({"solarSystem.id": 1});
    killmails.createIndex({"killTime": 1});

});