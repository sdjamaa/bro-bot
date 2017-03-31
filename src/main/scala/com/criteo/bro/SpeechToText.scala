package com.criteo.bro

import java.io.File
import collection.JavaConversions._

import com.ibm.watson.developer_cloud.speech_to_text.v1._
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.{SpeechAlternative, SpeechResults, Transcript}

object SpeechToText {
  val service: SpeechToText = new SpeechToText
  service.setUsernameAndPassword("1d782f20-5182-48a0-aa4b-c58fc1130549", "yylJfj2pYAZ7")

  def convertSpeechToText(file: String): String = {
    val audio: File = new File(file)

    val transcript: SpeechResults = service.recognize(audio).execute
    var outputString = ""
    val confidence: Double = 0
    for (item: Transcript <- transcript.getResults) {
      for (alternative: SpeechAlternative <- item.getAlternatives) {
        if (alternative.getConfidence > confidence)
          outputString = alternative.getTranscript
      }
    }
    outputString stripSuffix " "
  }

}
