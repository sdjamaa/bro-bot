package com.criteo.bro.service

object CriteoMessengerService {

  case class Product(id: Int, url: String, image: String, price: Double)

  def searchProducts(partnerId: Int, query: String): Iterable[Product] = ???

  def viewProduct(partnerId: Int, productId: Int) = ???

  def viewHome(partnerId: Int) = ???

  def addToCart(partnerId: Int, productId: Int) = ???

  def buyProduct(partnerId: Int, productId: Int) = ???

}
