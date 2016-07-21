package xyz.notarealtree.api

import io.finch._
import com.twitter.finagle.Http
import com.twitter.util.Await
import xyz.notarealtree.items.{ProcessingThread, Processor}
import com.fasterxml.jackson.databind.ObjectMapper
import xyz.notarealtree.caching.CacheProvider
import xyz.notarealtree.model.Response

/**
  * Created by Francis on 19/07/2016.
  */
object Api extends App{
    val port: String = "8079"
    val processor: Processor = new Processor()
    val objectMapper: ObjectMapper = new ObjectMapper()
    val cacheProvider: CacheProvider = null // This needs an actual implementation

    val api: Endpoint[String] = get("status") { Ok("Menagerie running on port " + port) }
    val topFit: Endpoint[String] = get("top" :: param("id")) {
        id: String =>
            val query = "top:" + id
            var result = ""
            if(cacheProvider.isCached(id)){
                result = cacheProvider.getCachedValue(query)
            }else{
                result = objectMapper.writeValueAsString(new Response(processor.processSomeMails(id.toInt)))
                cacheProvider.cacheValue(query, result)
            }
            Ok(result)
    }

    def init(): Unit ={
        val processingThread: ProcessingThread = new ProcessingThread(20000, processor)
        Await.ready(Http.server.serve(":" + port, (api :+: topFit).toServiceAs[Text.Plain]))
    }

    init()

}
