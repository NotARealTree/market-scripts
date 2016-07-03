/**
 * Created by Francis on 03/07/2016.
 */

var request = require('request');
var config = require('./config');
var MongoClient = require('mongodb').MongoClient
    , assert = require('assert');

var url = config.mongo.url;
MongoClient.connect(url, function(err, db) {
    assert.equal(null, err);

    var collection = db.collection('killmails');

    setInterval(function(){
        request('http://redisq.zkillboard.com/listen.php', (err, res, body) => {
            console.log(body);
            if(body != null){
                var json = JSON.parse(body);
                if(json.package != null){
                    var killmail = json.package.killmail;

                    killmail = parseKillMail(killmail);

                    collection.insert(killmail);
                }
            }
        });
    }, 1000);

});

function parseKillMail(killmail){
    killmail.killTime = new Date(killmail.killTime).getTime();
    killmail._id = killmail.killID;
    delete killmail.killID;

    deleteKeys(killmail,
        [
            "href",
            "icon",
            "corporation",
            "alliance",
            "id_str",
            "damageDone_str",
            "damageTaken_str",
            "securityStatus"
        ]
    );
    return killmail;
}

function deleteKeys(object, keys){
    keys.forEach(key => {
        deleteKey(object, key);
    });
    return object;
}

function deleteKey(object, key){
    if(key in object){
        delete object[key];
    }
    for(var field in object){
        if(object[field].constructor === Array){
            object[field].forEach(entry => {
                deleteKey(entry, key);
            });
        }else if(object[field].constructor === Object){
            deleteKey(object[field], key);
        }
    }
    return object;
}