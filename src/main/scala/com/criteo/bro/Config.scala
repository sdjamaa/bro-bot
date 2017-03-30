package com.criteo.bro

object Config {
  val FACEBOOK_URL = "graph.facebook.com:80"

  val PAGE_ACCESS_TOKEN = sys.env.get("PAGE_ACCESS_TOKEN").getOrElse("NONE")
}
