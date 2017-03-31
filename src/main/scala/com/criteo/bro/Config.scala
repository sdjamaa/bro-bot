package com.criteo.bro

object Config {
  val VALIDATION_TOKEN = sys.env.get("VALIDATION_TOKEN").getOrElse("DEFAULT_TOKEN")

  val FACEBOOK_URL = "graph.facebook.com:443"

  val PAGE_ACCESS_TOKEN = sys.env.get("PAGE_ACCESS_TOKEN").getOrElse("NONE")
}
