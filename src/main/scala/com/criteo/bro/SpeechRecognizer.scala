package com.criteo.bro

import java.nio.file.{Files, Path, Paths}
import java.util
import java.util.List

import com.google.cloud.speech.spi.v1beta1.SpeechClient
import com.google.cloud.speech.v1beta1.RecognitionConfig.AudioEncoding
import com.google.cloud.speech.v1beta1._
import com.google.protobuf.ByteString

object SpeechRecognizer {
  def convertSpeechToText(file: String) = {
    val speech: SpeechClient = SpeechClient.create

    val path: Path = Paths.get(file)
    val data: Array[Byte] = Files.readAllBytes(path)
    val audioBytes: ByteString = ByteString.copyFrom(data)

    val config: RecognitionConfig = RecognitionConfig.newBuilder
      .setEncoding(AudioEncoding.FLAC)
      .setSampleRate(44100)
      .build

    val audio: RecognitionAudio = RecognitionAudio.newBuilder
      .setContent(audioBytes)
      .build

    val response: SyncRecognizeResponse = speech.syncRecognize(config, audio)
    val results: List[SpeechRecognitionResult] = response.getResultsList

    speech.close()

    results.get(0).getAlternatives(0).getTranscript
  }
}
