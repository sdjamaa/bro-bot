package com.criteo.bro.service

import wabisabi.Client

object CriteoMessengerService {

  private val esClient = new Client("http://localhost:9200")

  case class Product(id: Int, url: String, image: String, price: Double)

  def searchProducts(partnerId: Int, query: String): Iterable[Product] = {
    List(Product(id = 10, url = "http://www.criteo.com/", image = "https://upload.wikimedia.org/wikipedia/en/thumb/5/53/Arsenal_FC.svg/870px-Arsenal_FC.svg.png", price = 24.3))
  }

  def viewProduct(partnerId: Int, productId: Int) = ???

  def viewHome(partnerId: Int) = ???

  def addToCart(partnerId: Int, productId: Int) = ???

  def buyProduct(partnerId: Int, productId: Int) = ???

}
