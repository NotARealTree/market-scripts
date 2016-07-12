/**
 * Created by Francis on 04/07/2016.
 */

var MongoClient = require('mongodb').MongoClient
    , assert = require('assert');
var url = 'mongodb://localhost:27017/tyche';

var jsonfile = require('jsonfile');

var systems = [30002050, 30002051, 30002052, 30002055, 30002056, 30002057, 30002058, 30002059, 30002060, 30002061, 30002062, 30002063, 30002064, 30002065, 30002066, 30002067, 30002073, 30002074, 30002076, 30002077, 30002078, 30002079, 30002080, 30002081, 30002082, 30002083, 30002084, 30002085, 30002086, 30002087, 30002088, 30002089, 30002090, 30002091, 30002092, 30002093, 30002094, 30002095, 30002096, 30002097, 30002098, 30002099, 30002100, 30002101, 30002102, 30003424, 30003425, 30003426, 30003444, 30003446, 30003460, 30003461, 30003463, 30003465, 30003466, 30003467, 30003470, 30003471];

var program = require('commander');

program
    .version("0.0.1")
    .option('-g, --groups <groups>', 'Inventory Group(s) to limit the search to', list)
    .option('-l, --limit <number>', 'Maximum number of results to be returned', parseInt)
    .option('-s, --sort <number>', 'Sort direction (default descending)', parseInt)
    .option('-f, --from <number>', 'Include only mails from (epoch ts)', parseInt)
    .option('-t, --to <number>', 'Include only mails to (epoch ts)', parseInt)
    .parse(process.argv);

console.log(program.groups);
console.log(program.limit);

function list(val) {
    return val.split(',');
}

var start = new Date().getTime();

MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);

    var groups = db.collection('groups');
    var killmails = db.collection('killmails');

    groups.find({categoryName: {$in: program.groups}}).toArray((err, res) => {
        var groupIds = [];
        res.forEach(result => {
            groupIds.push(result.groupId);
        });
        parseItems(killmails, groupIds);
    });

});


function parseItems(collection, groupIds){
    collection.aggregate([
        {$match: {
            "solarSystem.id": {$in: systems},
            killTime: {$gte: program.from || 0},
            killTime: {$lte: program.to || new Date().getTime()}
        }},
        {$unwind: "$victim.items"},
        {$project: {
            itemId: "$victim.items.itemType.id",
            groupId: "$victim.items.groupId",
            name: "$victim.items.itemType.name",
            dropped: { $ifNull: [ "$victim.items.quantityDropped", 0]},
            destroyed: { $ifNull: [ "$victim.items.quantityDestroyed", 0]}}
        },
        {$project: {
            _id: 1,
            itemId: 1,
            name: 1,
            groupId: 1,
            sum: {$add: ["$dropped", "$destroyed"]}}
        },
        {$group: {
            _id: "$itemId",
            groupId: "$groupId",
            name: {$first: "$name"},
            amount: {$sum: "$sum"}}
        },
        {$match: {groupId: {$in: "$groupId"}}},
        {$sort: {amount: program.sort || -1}},
        {$limit: program.limit }
    ], (err, res) => {
        var meta = {
            queryStart: start,
            queryCompletion: new Date().getTime(),
            groups: program.groups,
            limit: program.limit,
            sort: program.sort,
            to: program.to,
            from: program.from,
            systems: systems
        };

        var output = {
            meta: meta,
            results: res
        };

        jsonfile.writeFile('queryResults-'+ new Date().getTime() +'.json', output, function (err) {
            process.exit();
        });

    });
}