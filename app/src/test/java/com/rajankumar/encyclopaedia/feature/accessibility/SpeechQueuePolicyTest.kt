package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechQueuePolicyTest {
  @Test fun lessonsAppendChunks() = assertEquals(SpeechQueuePolicy.APPEND, defaultSpeechQueuePolicy(SpeechContentKind.LESSON))
  @Test fun questionsReplaceCurrentSpeech() = assertEquals(SpeechQueuePolicy.REPLACE, defaultSpeechQueuePolicy(SpeechContentKind.QUESTION))
}