package com.criteo.bro

import org.scalatest.{FlatSpec, Matchers}

class VoiceRecognizer extends FlatSpec with Matchers {
  it should "match" in {
    val result = SpeechToText.convertSpeechToText("/users/a.shamim/Downloads/test.wav")
    result shouldEqual "can I get a beer"
  }
}
