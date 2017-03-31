package com.criteo.bro

import org.scalatest.{FlatSpec, Matchers}

class VoiceRecognizer extends FlatSpec with Matchers {
  it should "match" in {
    val result = SpeechRecognizer.convertSpeechToText("test.flac")
    result shouldEqual "can I get a beer"
  }
}
