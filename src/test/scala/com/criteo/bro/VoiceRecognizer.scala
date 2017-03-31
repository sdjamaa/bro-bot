package com.criteo.bro

import org.scalatest.{FlatSpec, Matchers}

class VoiceRecognizer extends FlatSpec with Matchers {
  it should "match" in {
    val result = SpeechToText.convertSpeechToText("test.wav")
    result shouldEqual "can I get a beer"
  }

  it should "match2" in {
    val result = SpeechToText.convertAudioUrlToText("https://aacapps.com/lamp/sound/amy.mp3")
    val expectedResult = "hi I'm Amy one of the available high quality text to speech voices select download now to install my voice"

    result shouldEqual expectedResult
  }
}
