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

    def processSomeMails() : Unit = {
        val mongodb : MongoDB = new MongoDB("localhost:27017", "tyche", "killmails", "groups")
        val mails = mongodb.retrieveMails()
        fillMaps(mails)
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
        val sorted = counts.toList.sortBy(_._2).reverse
        for(i <- 0 to 5){
            print(sorted(i)_2)
            println(objects(sorted(i)_1))
        }
    }
}
