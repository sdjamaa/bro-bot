package com.criteo.bro.service

import com.twitter.finatra.http.response.ResponseBuilder

object PageSubscriptionService {

  def handleReadMessage(readMessage: Map[String, Any]) = {
    println(s"Watermark: ${readMessage.get("watermark").get} || seq: ${readMessage.get("seq").get}")
  }

  def handlePageSubscription(response: ResponseBuilder, entries: List[Map[String, Any]]) = {
    for {
      entry <- entries
      messaging <- entry.get("messaging").get.asInstanceOf[List[Map[String, Any]]]
    } yield {
      if (messaging.get("read").isDefined) {
        handleReadMessage(messaging.get("read").get.asInstanceOf[Map[String, Any]])
      }
    }

    response.ok("YOLO")
  }

}
