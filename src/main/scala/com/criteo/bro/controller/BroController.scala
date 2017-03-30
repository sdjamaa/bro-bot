package com.criteo.bro.controller

import com.criteo.bro.Config
import com.criteo.bro.service.PageSubscriptionService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.json4s._
import org.json4s.jackson.JsonMethods._

class BroController extends Controller {

  def jsonStrToMap(jsonStr: String): Map[String, Any] = {
    implicit val formats = org.json4s.DefaultFormats

    parse(jsonStr).extract[Map[String, Any]]
  }

  get("/webhook") { request: Request =>
    if ((request.params.get("hub.mode") == "subscribe") && (request.params("hub.verify_token") == Config.VALIDATION_TOKEN)) {
      response.ok(request.params.get("hub.challenge"))
    } else {
      println("Failed validation. Make sure the validation tokens match.")
      response.forbidden
    }
  }

  post("/webhook") { request: Request =>
    val jsonMap = jsonStrToMap(request.contentString)

      jsonMap.get("object") match {
      case Some("page") => PageSubscriptionService.handlePageSubscription(response, jsonMap.get("entry").get.asInstanceOf[List[Map[String, Any]]])
      case _ => response.ok
    }
  }
}
