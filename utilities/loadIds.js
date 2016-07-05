/**
 * Created by Francis on 05/07/2016.
 */

var MongoClient = require('mongodb').MongoClient
    , assert = require('assert');
var fs = require('fs');

var url = 'mongodb://localhost:27017/tyche';



MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);

    fs.readFile( __dirname + '../resources/invTypes.csv', function (err, data) {
        if (err) {
            throw err;
        }

        var objects = [];

        var lines = data.split('\n');
        lines.forEach(line => {
            var splits = line.split(',');
            objects.push({
                typeId: splits[0],
                groupId: splits[1],
                typeName: splits[2]
            });
        });

        var items = db.collection('items');
        items.insertMany(objects);
    });
});