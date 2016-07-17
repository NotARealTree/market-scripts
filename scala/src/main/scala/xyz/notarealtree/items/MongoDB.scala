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
            val slots = getSlots(items, groups)

            killmail.ship = get[Int](List("victim", "shipType", "id"), item)
            killmail.drones = slots(DRONES)
            killmail.lows = slots(LOWS)
            killmail.mids = slots(MIDS)
            killmail.highs = slots(HIGHS)
            killmail.rigs = slots(RIGS)

            result = result :+ killmail
        }
        result
    }

    def getSlots(items: BasicDBList, groups: List[Int]) : Map[String, List[Int]] = {
        var result = Map[String, List[Int]](
            LOWS -> List(),
            MIDS -> List(),
            HIGHS -> List(),
            RIGS -> List(),
            DRONES -> List()
        )

        for(i <- 0 until items.size()){
            val item = items.get(i).asInstanceOf[Imports.DBObject]
            val flag = get[Int](List("flag"), item)
            val id = get[Int](List("itemType", "id"), item)
            val groupId = get[String](List("groupId"), item).toInt

            // TODO: This doesnt include drones currently T_T

            if(rigs.contains(flag)){
                result = result + (RIGS -> (result(RIGS) :+ id))
            }else if(lows.contains(flag) && groups.contains(groupId)){
                result = result + (LOWS -> (result(LOWS) :+ id))
            }else if(mids.contains(flag) && groups.contains(groupId)){
                result = result + (MIDS -> (result(MIDS) :+ id))
            }else if(highs.contains(flag) && groups.contains(groupId)){
                result = result + (HIGHS -> (result(HIGHS) :+ id))
            }else if(drones.contains(flag) && groups.contains(groupId)){
                result = result + (DRONES -> (result(DRONES) :+ id))
            }
        }
        result
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
