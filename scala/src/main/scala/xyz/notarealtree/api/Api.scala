package xyz.notarealtree.api

import io.finch._
import com.twitter.finagle.Http
import com.twitter.util.Await
import xyz.notarealtree.items.Processor
import com.fasterxml.jackson.databind.ObjectMapper
import xyz.notarealtree.model.Response

/**
  * Created by Francis on 19/07/2016.
  */
object Api extends App{
    val port: String = "8079"
    val processor: Processor = new Processor()
    val objectMapper: ObjectMapper = new ObjectMapper()

    val api: Endpoint[String] = get("status") { Ok("Menagerie running on port " + port) }
    val topFit: Endpoint[String] = get("top" :: param("id")) {
        id: String =>
            Ok(objectMapper.writeValueAsString(new Response(processor.processSomeMails(id.toInt))))
    }

    Await.ready(Http.server.serve(":" + port, (api :+: topFit).toServiceAs[Text.Plain]))
}
