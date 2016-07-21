package xyz.notarealtree.items

import com.mongodb.BasicDBList
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{Imports, MongoClient}

import scala.reflect.ClassTag

/**
  * Created by Francis on 16/07/2016.
  */
class MongoDB(val mongoUrl: String, val dbName: String, val kmCollection: String, val gpCollection: String, val itCollection: String) {
    private val mongoClient: MongoClient = MongoClient(mongoUrl)
    private val database = mongoClient(dbName)
    private val killmailCollection = database(kmCollection)
    private val groupCollection = database(gpCollection)
    private val itemCollection = database(itCollection)
    private val rigs : List[Int] = List.range(92, 95)
    private val highs : List[Int] = List.range(27, 35)
    private val mids : List[Int] = List.range(19, 27)
    private val lows : List[Int] = List.range(11, 19)
    private val drones : List[Int] = List(87)
    private val cargo : List[Int] = List(5)
    private val DRONES : String = "drones"
    private val LOWS : String = "lows"
    private val MIDS : String = "mids"
    private val HIGHS : String = "highs"
    private val RIGS : String = "rigs"


    def retrieveMails() : List[Killmail] = {
        val allDocs = killmailCollection.find()
        val groups = loadGroups()
        toKillmailObjectList(allDocs.toArray, groups)
    }

    def loadGroups() : List[Int] = {
        var result = List[Int]()
        val query = MongoDBObject(
            "$or" -> List(
                MongoDBObject("categoryId" -> "7"),
                MongoDBObject("categoryId" -> "18")
            )
        )
        val allGroups = groupCollection.find(query).toArray
        for(group <- allGroups){
            val groupId = get[String](List("groupId"), group).toInt
            result = result :+ groupId
        }
        result
    }

    def sort(killmail: List[Killmail]) : Unit = {
        killmail.foreach(killmail => killmail.sort())
    }

    def toKillmailObjectList(array: Array[Imports.DBObject], groups: List[Int]) : List[Killmail] = {
        var result = List[Killmail]()

        for(item <- array){
            val killmail = new Killmail()
            val items = get[BasicDBList](List("victim", "items"), item)

            killmail.ship = get[Int](List("victim", "shipType", "id"), item)

            val (l, m, h, r, d) = getSlots(items, groups)

            killmail.lows = l
            killmail.mids = m
            killmail.highs = h
            killmail.rigs = r
            killmail.drones = d

            result = result :+ killmail
        }
        result
    }

    def getSlots(items: BasicDBList, groups: List[Int]) : (List[Int], List[Int], List[Int], List[Int], List[(Int, Int)]) = {
        var lowslots = List[Int]()
        var midslots = List[Int]()
        var highslots = List[Int]()
        var rigslots = List[Int]()
        var droneTuples = List[(Int, Int)]()

        for(i <- 0 until items.size()){
            val item = items.get(i).asInstanceOf[Imports.DBObject]
            val flag = get[Int](List("flag"), item)
            val id = get[Int](List("itemType", "id"), item)
            val groupId = get[String](List("groupId"), item).toInt
            if(rigs.contains(flag)){
                rigslots = rigslots :+ id
            }else if(lows.contains(flag) && groups.contains(groupId)){
                lowslots = lowslots :+ id
            }else if(mids.contains(flag) && groups.contains(groupId)){
                midslots = midslots :+ id
            }else if(highs.contains(flag) && groups.contains(groupId)){
                highslots = highslots :+ id
            }else if(drones.contains(flag) && groups.contains(groupId)){
                val qty = get[Int](List("quantityDropped"), item) + get[Int](List("quantityDestroyed"), item)
                val tuple = (id, qty)
                droneTuples = droneTuples :+ tuple
            }
        }

        (lowslots, midslots, highslots, rigslots, droneTuples)
    }

    def getItems(): Map[Long, String] = {
        val allItems = itemCollection.find().toArray
        var result = Map[Long, String]()

        for(item <- allItems){
            val itemId = get[String](List("typeId"), item).toLong
            val itemName = get[String](List("typeName"), item)
            result = result + (itemId -> itemName)
        }
        result
    }

    def get[A:ClassTag](keys: List[String], dBObject: Imports.DBObject) : A = {
        if(keys.length > 1){
            get(keys.tail, dBObject.get(keys.head).asInstanceOf[Imports.DBObject])
        }else{
            dBObject.get(keys.head).asInstanceOf[A]
        }
    }

}