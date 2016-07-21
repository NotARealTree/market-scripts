package xyz.notarealtree.items

/**
  * Created by Francis on 21/07/2016.
  */
class ProcessingThread(val interval: Long, val processor: Processor) extends Runnable{
    override def run(): Unit = {
        while(!Thread.currentThread().isInterrupted){

            Thread.sleep(interval)
        }
    }
}
