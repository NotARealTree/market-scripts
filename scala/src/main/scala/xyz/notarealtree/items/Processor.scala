package xyz.notarealtree.items

/**
  * Created by Francis on 16/07/2016.
  */
class Processor {
    private val rigs : List[Int] = List.range(92, 94)
    private val highs : List[Int] = List.range(27, 34)
    private val mids : List[Int] = List.range(19, 26)
    private val lows : List[Int] = List.range(11, 18)
    private val drones : List[Int] = List(87)
    private val cargo : List[Int] = List(5)
    private var counts : Map[String, Int] = Map()
    private var objects : Map[String, Killmail] = Map()
    private val randomNameGenerator: RandomNameGenerator = new RandomNameGenerator()
    val mongodb : MongoDB = new MongoDB("localhost:27017", "tyche", "killmails", "groups", "items")

    def processSomeMails(id: Int) : String = {
        val mails = mongodb.retrieveMails()
        val items = mongodb.getItems()
        fillMaps(mails)
        convertFits(items)(id)
    }

    def fillMaps(killmails: List[Killmail]): Unit = {
        killmails.foreach(killmail => {
            val serialized: String = killmail.serialize()
            // If it's contained in one it has to be contained in the other
            if(objects.contains(serialized)){
                counts = counts + (serialized -> (counts(serialized) + 1))
            }else{
                objects = objects + (serialized -> killmail)
                counts = counts + (serialized -> 1)
            }
        })
    }

    def convertFits(items: Map[Long, String]): List[String] = {
        val sorted = counts.toList.sortBy(_._2).reverse
        var fits: List[String] = List[String]()
        for(i <- 1 to 1){
            fits = fits :+ convertFit(objects(sorted(i)_1), items)
        }
        fits
    }

    def convertFit(killmail: Killmail, items: Map[Long, String]): String = {
        val sb = StringBuilder.newBuilder
        sb.append("[" + items(killmail.ship) + ", " + randomNameGenerator.generateRandomName() + "]\\n\\n")

        concat(killmail.lows, sb, items)
        concat(killmail.mids, sb, items)
        concat(killmail.highs, sb, items)
        concat(killmail.rigs, sb, items)
        concatDrones(killmail.drones, sb, items)

        sb.toString()
    }

    def concat(ls: List[Int], sb: StringBuilder, items: Map[Long, String]): Unit = {
        if(ls.nonEmpty){
            ls.foreach(slot => {
                sb.append(items(slot) + "\\n")
            })
            sb.append("\\n")
        }
    }

    def concatDrones(ls: List[(Int, Int)], sb: StringBuilder, items: Map[Long, String]): Unit = {
        if(ls.nonEmpty){
            ls.foreach(slot => {
                sb.append(items(slot _1) + " x" + (slot _2) + "\\n")
            })
            sb.append("\\n")
        }
    }
}