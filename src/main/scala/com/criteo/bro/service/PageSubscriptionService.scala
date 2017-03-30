package com.criteo.bro.service

import com.criteo.bro.Config
import com.criteo.bro.model._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Method.Post
import com.twitter.finagle.http.{Http, Request}
import com.twitter.finatra.http.response.ResponseBuilder

import org.json4s._
import org.json4s.jackson.Serialization.{read,write}

object PageSubscriptionService {

  def handleReadMessage(readMessage: Map[String, Any]) = {
    println(s"Watermark: ${readMessage.get("watermark").get} || seq: ${readMessage.get("seq").get}")
  }

  def respondWithText(recipientId: String, msg: String) = {
    val client = ClientBuilder()
      .codec(Http())
      .hosts(Config.FACEBOOK_URL)
      .hostConnectionLimit(1)
      .build()

    val req = Request(Post, s"/me/messages?access_token=${Config.PAGE_ACCESS_TOKEN}")

    val elts: List[FacebookGenericElement] = List(FacebookGenericElement(
      title = "product_title",
      subtitle = msg,
      image_url = "https://upload.wikimedia.org/wikipedia/en/thumb/5/53/Arsenal_FC.svg/870px-Arsenal_FC.svg.png",
      default_action = DefaultAction(actiontype = "web_url", title = "button", url = "http://www.criteo.com/"),
      buttons = List(FacebookButton(btntype = "payment", title = "Please buy", payload = "LOLILOL", payment_summary =
        PaymentSummary(
          currency = "USD",
          is_test_payment = true,
          payment_type = "FIXED_AMOUNT",
          merchant_name = "Criteo merchant",
          requested_user_info = List(RequestedUserInfo(shipping_address = "325 Lytton Av", contact_name = "Yacine Achiakh", contact_phone = "555-555-5555", contact_email = "y.achiakh@criteo.com")),
          price_list = List(Price(label = "product1", amount = "12.34"))
        )))
    ))

    val msgResponse = FacebookResponse(
      recipient = Recipient(id = recipientId),
      message = FacebookMessage(attachment = FacebookAttachment(
        atttype = "template",
        payload = FacebookPayload(template_type = "generic", elements = elts)
      ))
    )
    implicit val formats = DefaultFormats

    req.contentString_=(write(msgResponse))
    val f = client(req)

    // Handle the response:
    f onSuccess { res =>
      println("got response", res)
    } onFailure { exc =>
      println("failed :-(", exc)
    }
  }

  def handleMessage(recipientId: String, message: Map[String, Any]) = {
    val msg = message.get("text").get.asInstanceOf[String]

    respondWithText(recipientId, msg.substring(0, Math.min(19, msg.length - 1)))
  }

  def handlePageSubscription(response: ResponseBuilder, entries: List[Map[String, Any]]) = {
    for {
      entry <- entries
      messaging <- entry.get("messaging").get.asInstanceOf[List[Map[String, Any]]]
    } yield {

      val recipientId = messaging.get("recipient").get.asInstanceOf[Map[String, String]].get("id").get

      if (messaging.get("message").isDefined) {
        handleMessage(recipientId, messaging.get("message").get.asInstanceOf[Map[String, Any]])
      }
      if (messaging.get("read").isDefined) {
        handleReadMessage(messaging.get("read").get.asInstanceOf[Map[String, Any]])
      }
    }

    response.ok("YOLO")
  }

}
