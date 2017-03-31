package com.criteo.bro

object KeywordFilter {
  val phrases = List("I want", "Do you have", "Is there", "Can I buy", "Can I get", "Can I", "Hello", "Hi")
  val phraseRegex = phrases.tail.foldLeft(phrases.head)((a, b) => s"$a|$b")
  val phraseKeywordRegex = s"""($phraseRegex)\\s+(a\\s+)?(([a-z]+ )*[a-z]+)""".r

  def extractKeywords(message: String): String = {
    val input = message.replaceAll("""[\p{Punct}]""", "")
    input match {
      case phraseKeywordRegex(_, _, keywords, _) => keywords
      case _ => ""
    }
  }
}
