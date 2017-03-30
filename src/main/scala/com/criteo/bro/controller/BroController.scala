package com.criteo.bro.controller

import com.criteo.bro.BroTools
import com.criteo.bro.Config
import com.criteo.bro.service.PageSubscriptionService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class BroController extends Controller {

  post("/webhook") { request: Request =>
    val jsonMap = BroTools.jsonStrToMap(request.contentString)

      jsonMap.get("object") match {
      case Some("page") => PageSubscriptionService.handlePageSubscription(response, jsonMap.get("entry").get.asInstanceOf[List[Map[String, Any]]])
      case _ => response.ok
    }
  }
}
