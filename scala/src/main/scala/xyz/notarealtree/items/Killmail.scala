package xyz.notarealtree.items

/**
  * Created by Francis on 16/07/2016.
  */
class Killmail {
    var highs: List[Int] = _
    var mids: List[Int] = _
    var lows: List[Int] = _
    var rigs: List[Int] = _
    var drones: List[(Int, Int)] = _
    var ship: Int = _

    override def toString = s"Killmail(highs=$highs, mids=$mids, lows=$lows, rigs=$rigs, drones=$drones, ship=$ship)"

    def sort(): Unit = {
        highs = highs.sortWith(_ > _)
        mids = mids.sortWith(_ > _)
        lows = lows.sortWith(_ > _)
        rigs = rigs.sortWith(_ > _)
        drones = drones.sortWith(_._1 > _._1)
    }

    def serialize(): String = {
        ship + join(highs) + join(mids) + join(lows) + join(rigs) + join(drones.map(x => x _1))
    }

    //TODO: This should be pattern matching really
    def join(ls: List[Int]) : String = {
        if(ls.length > 1){
            ls.head + join(ls.tail)
        }else{
            ""
        }
    }
}
