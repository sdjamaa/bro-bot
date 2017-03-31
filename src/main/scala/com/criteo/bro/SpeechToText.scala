package com.criteo.bro

import java.io.File
import java.net.URL
import sys.process._

import collection.JavaConversions._
import org.apache.commons.io.FileUtils
import com.ibm.watson.developer_cloud.speech_to_text.v1._
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.{SpeechAlternative, SpeechResults, Transcript}

object SpeechToText {
  val service: SpeechToText = new SpeechToText
  service.setUsernameAndPassword("1d782f20-5182-48a0-aa4b-c58fc1130549", "yylJfj2pYAZ7")

  def convertAudioUrlToText(urlString: String): String = {
    val url = new URL(urlString)
    val extension = urlString substring urlString.lastIndexOf(".")
    val tDir = System.getProperty("java.io.tmpdir")
    val path = tDir + "tmp" + extension
    val wavPath = tDir + "tmp" + ".wav"
    val file = new File(path)
    file.deleteOnExit

    try {
      FileUtils.copyURLToFile(url, file)
      s"ffmpeg -i $path $wavPath" !
      val wavFile = new File(wavPath)
      wavFile.deleteOnExit
      convertSpeechToText(wavFile)
    } catch {
      case e: Exception => ""
    }
  }

  def convertSpeechToText(file: String): String = {
    val audio: File = new File(file)
    convertSpeechToText(audio)
  }

  def convertSpeechToText(audio: File): String = {
    try {
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
    } catch {
      case e: Exception => ""
    }
  }
}
