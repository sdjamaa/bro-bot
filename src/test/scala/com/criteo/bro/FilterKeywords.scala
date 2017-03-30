package com.criteo.bro

import org.scalatest.{FlatSpec, Matchers}

class FilterKeywords extends FlatSpec with Matchers {

  it should "match1" in {
    val input = "I want a grey, sweatshirt"
    val output = KeywordFilter.extractKeywords(input)
    val expectedOutput = List("grey", "sweatshirt")
    output shouldEqual expectedOutput
  }

  it should "match2" in {
    val input = "Do you have a blue printer?"
    val output = KeywordFilter.extractKeywords(input)
    val expectedOutput = List("blue", "printer")
    output shouldEqual expectedOutput
  }

}
