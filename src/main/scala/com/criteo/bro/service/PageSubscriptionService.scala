package com.criteo.bro.service

import com.criteo.bro.Config
import com.criteo.bro.model._
import com.criteo.bro.service.CriteoMessengerService.Product
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Http, Request, RequestBuilder}
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.io.Buf.{ByteArray, Utf8}
import org.json4s._
import org.json4s.jackson.Serialization.write
import org.json4s.FieldSerializer._

object PageSubscriptionService {

  def handleReadMessage(readMessage: Map[String, Any]) = {
    println(s"Watermark: ${readMessage.get("watermark").get} || seq: ${readMessage.get("seq").get}")
  }

  def respondWithGenericTemplate(recipientId: String, msg: String, elts: List[FacebookGenericElement]) = {
    val client = ClientBuilder()
      .codec(Http())
      .hosts(Config.FACEBOOK_URL)
      .hostConnectionLimit(1)
      .tlsWithoutValidation()
      .build()

    val msgResponse = FacebookResponse(
      recipient = Recipient(id = recipientId),
      message = FacebookMessage(attachment = FacebookAttachment(
        atttype = "template",
        payload = FacebookPayload(template_type = "generic", elements = elts)
      ))
    )

    val renameDefaultActionType = FieldSerializer[DefaultAction](renameTo("actiontype", "type"), renameFrom("type", "actiontype"))
    val renameFacebookButtonType = FieldSerializer[FacebookButton](renameTo("btntype", "type"), renameFrom("type", "btntype"))
    val renameAttachmentType = FieldSerializer[FacebookAttachment](renameTo("atttype", "type"), renameFrom("type", "atttype"))

    implicit val formats = DefaultFormats + renameDefaultActionType + renameFacebookButtonType + renameAttachmentType

    val contentStr = write(msgResponse)

    val req = RequestBuilder()
      .addHeader("Content-Type", "application/json")
      .url(s"https://${Config.FACEBOOK_URL}/me/messages?access_token=${Config.PAGE_ACCESS_TOKEN}")
      .buildPost(Utf8(contentStr))

    val f = client(req)

    // Handle the response:
    f onSuccess { res =>
      println("got response", res)
    } onFailure { exc =>
      println("failed :-(", exc)
    }
  }

  def createFacebookElementFromProduct(products: Iterable[Product]) = {
    for (product <- products) yield {
      FacebookGenericElement(
        title = s"${product.title}",
        subtitle = s"${product.price} ${product.currency}",
        image_url = product.image,
        default_action = DefaultAction(actiontype = "web_url", url = product.url),
        buttons = List(FacebookButton(btntype = "web_url", title = "Buy", url = product.url))
      )
    }
  }

  def handleMessage(recipientId: String, message: Map[String, Any]) = {
    val msg = message.get("text").get.asInstanceOf[String]

    // call to ES
    val products = CriteoMessengerService.searchProducts(1, msg)

    val genericElts = createFacebookElementFromProduct(products).toList

    respondWithGenericTemplate(recipientId, msg.substring(0, Math.min(19, msg.length - 1)), genericElts)
  }

  def handlePageSubscription(response: ResponseBuilder, entries: List[Map[String, Any]]) = {
    for {
      entry <- entries
      messaging <- entry.get("messaging").get.asInstanceOf[List[Map[String, Any]]]
    } yield {

      val recipientId = messaging.get("sender").get.asInstanceOf[Map[String, String]].get("id").get

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
