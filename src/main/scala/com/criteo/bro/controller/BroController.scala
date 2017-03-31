package com.criteo.bro.controller

import com.criteo.bro.BroTools
import com.criteo.bro.Config
import com.criteo.bro.service.{CriteoMessengerService, PageSubscriptionService, RecipientCacheService}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller


class BroController extends Controller {

  get("/webhook") { request: Request =>
    if ((request.params.get("hub.mode").get == "subscribe") &&
      (request.params.get("hub.verify_token").get == Config.VALIDATION_TOKEN)) {
      response.ok(request.params.get("hub.challenge").get)
    } else {
      println("Failed validation. Make sure the validation tokens match.")
      response.forbidden
    }
  }

  post("/webhook") { request: Request =>
    val jsonMap = BroTools.jsonStrToMap(request.contentString)

    jsonMap.get("object") match {
      case Some("page") => PageSubscriptionService.handlePageSubscription(response, jsonMap.get("entry").get.asInstanceOf[List[Map[String, Any]]])
      case _ => response.ok
    }
  }

  post("/recommendation") { request: Request =>
    val products = BroTools.jsonStrToClass[List[CriteoMessengerService.Product]](request.contentString)

    if (RecipientCacheService.latestRecipient != "") {
      PageSubscriptionService.sendMessage(RecipientCacheService.latestRecipient, products)
    }

    response.ok
  }
}
