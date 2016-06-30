/**
 * Created by Francis on 30/06/2016.
 */

var mail = {
    "killID": 54878417,
    "solarSystemID": 30045342,
    "killTime": "2016-06-30 20:47:30",
    "moonID": 0,
    "victim": {
        "shipTypeID": 17841,
        "characterID": 1505305062,
        "characterName": "Mattin241",
        "corporationID": 98259086,
        "corporationName": "Chef's Choice",
        "allianceID": 99001742,
        "allianceName": "Templis CALSF",
        "factionID": 500001,
        "factionName": "Caldari State",
        "damageTaken": 4123
    },
    "attackers": [{
        "characterID": 92640734,
        "characterName": "Joseph Guillotine",
        "corporationID": 420259384,
        "corporationName": "CTRL-Q",
        "allianceID": 99004743,
        "allianceName": "Spaceship Bebop",
        "factionID": 500004,
        "factionName": "Gallente Federation",
        "securityStatus": -10,
        "damageDone": 4123,
        "finalBlow": 1,
        "weaponTypeID": 3178,
        "shipTypeID": 17841
    }],
    "items": [{"typeID": 33890, "flag": 93, "qtyDropped": 0, "qtyDestroyed": 1, "singleton": 0}, {
        "typeID": 5973,
        "flag": 19,
        "qtyDropped": 1,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 2205, "flag": 87, "qtyDropped": 0, "qtyDestroyed": 3, "singleton": 0}, {
        "typeID": 33076,
        "flag": 11,
        "qtyDropped": 1,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 10190, "flag": 13, "qtyDropped": 0, "qtyDestroyed": 1, "singleton": 0}, {
        "typeID": 3178,
        "flag": 29,
        "qtyDropped": 0,
        "qtyDestroyed": 1,
        "singleton": 0
    }, {"typeID": 12614, "flag": 29, "qtyDropped": 63, "qtyDestroyed": 0, "singleton": 0}, {
        "typeID": 33890,
        "flag": 94,
        "qtyDropped": 0,
        "qtyDestroyed": 1,
        "singleton": 0
    }, {"typeID": 28668, "flag": 11, "qtyDropped": 0, "qtyDestroyed": 3, "singleton": 0}, {
        "typeID": 12614,
        "flag": 27,
        "qtyDropped": 63,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 10190, "flag": 14, "qtyDropped": 1, "qtyDestroyed": 0, "singleton": 0}, {
        "typeID": 12612,
        "flag": 5,
        "qtyDropped": 1226,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 12614, "flag": 5, "qtyDropped": 0, "qtyDestroyed": 1126, "singleton": 0}, {
        "typeID": 4025,
        "flag": 21,
        "qtyDropped": 1,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 3178, "flag": 27, "qtyDropped": 0, "qtyDestroyed": 1, "singleton": 0}, {
        "typeID": 2048,
        "flag": 12,
        "qtyDropped": 0,
        "qtyDestroyed": 1,
        "singleton": 0
    }, {"typeID": 33890, "flag": 92, "qtyDropped": 0, "qtyDestroyed": 1, "singleton": 0}, {
        "typeID": 28668,
        "flag": 5,
        "qtyDropped": 55,
        "qtyDestroyed": 0,
        "singleton": 0
    }, {"typeID": 23009, "flag": 5, "qtyDropped": 1278, "qtyDestroyed": 0, "singleton": 0}, {
        "typeID": 5443,
        "flag": 20,
        "qtyDropped": 1,
        "qtyDestroyed": 0,
        "singleton": 0
    }],
    "position": {"y": -27702067657.434, "x": 709310536252.84, "z": 678183556766.21},
    "zkb": {
        "locationID": 40349703,
        "hash": "fe14163428b12fa1c9a4a91e09439ac493b9aee3",
        "totalValue": 24707904.68,
        "points": 28
    }
};

function parseKillMail(killmail) {
    var items = {};

    killmail.items.forEach(function(item){
        addAmount(items, item.typeID, item.qtyDropped + item.qtyDestroyed);
    });

    addAmount(items, killmail.victim.shipTypeID, 1);

    return items;
}

function addAmount(map, id, amount){
    if(id in map){
        map[id] += amount;
    }else{
        map[id] = amount;
    }
}

var res = parseKillMail(mail);

console.log(res);