package com.criteo.bro.service

import scala.concurrent.ExecutionContext.Implicits.global
import com.criteo.bro.{BroTools, KeywordFilter}
import wabisabi.Client

import scala.concurrent.Future

object CriteoMessengerService {

  private val esClient = new Client("http://172.28.46.136:9200")

  case class Product(id: Int, title: String, url: String, image: String, price: Double, currency: String)

  def searchProducts(partnerId: Int, query: String): Iterable[Product] = {
    println(query)
    val keywords = KeywordFilter.extractKeywords(query)
    println(keywords)
    val results = esClient.search("catalog", "{\"query\": {\"match\": {\"body\": \""+keywords+"\"}}}").map(_.getResponseBody)
    val jsonResults: Future[Map[String, Any]] = results.map(BroTools.jsonStrToMap)
    while (!jsonResults.isCompleted) {}

    val rawProducts = jsonResults.value.get.get.get("hits").get.asInstanceOf[Map[String, Any]].get("hits").get.asInstanceOf[List[Map[String, Any]]]
    val products = rawProducts.map(p => p.get("_source").get.asInstanceOf[Map[String, Any]]).map(p => new Product(
      p.get("external_id").get.asInstanceOf[String].toInt,
      p.get("adwords_title").get.asInstanceOf[String],
      p.get("adwords_link").get.asInstanceOf[String],
      p.get("adwords_image_link").get.asInstanceOf[String],
      p.get("adwords_price").get.asInstanceOf[String].toDouble,
      p.get("adwords_product_price_currency").get.asInstanceOf[String]))
    println(products)
    products
  }

  def viewProduct(partnerId: Int, productId: Int) = ???

  def viewHome(partnerId: Int) = ???

  def addToCart(partnerId: Int, productId: Int) = ???

  def buyProduct(partnerId: Int, productId: Int) = ???

}
