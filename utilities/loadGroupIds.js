/**
 * Created by Francis on 05/07/2016.
 */

var MongoClient = require('mongodb').MongoClient
    , assert = require('assert');
var fs = require('fs');

var url = 'mongodb://localhost:27017/tyche';

MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);

    fs.readFile( __dirname + '/../resources/invGroups.csv', 'utf8', function (err, data) {
        if (err) {
            throw err;
        }

        var objects = [];

        var lines = data.split('\n');
        lines.forEach(line => {
            var splits = line.replace('\r', '').split(',');
            objects.push({
                groupId: splits[0],
                groupName: splits[1].toLowerCase(),
                categoryName: splits[2].toLowerCase()
            });
        });

        var groups = db.collection('groups');
        groups.drop();
        groups.insertMany(objects);
    });
});