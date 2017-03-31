package com.criteo.bro

import org.json4s.jackson.JsonMethods._

/**
  * Created by j.ma on 3/30/17.
  */
object BroTools {
  def jsonStrToMap(jsonStr: String): Map[String, Any] = {
    implicit val formats = org.json4s.DefaultFormats

    parse(jsonStr).extract[Map[String, Any]]
  }

  def jsonStrToClass[A : Manifest](jsonStr: String): A = {
    implicit val formats = org.json4s.DefaultFormats

    parse(jsonStr).extract[A]
  }
}
