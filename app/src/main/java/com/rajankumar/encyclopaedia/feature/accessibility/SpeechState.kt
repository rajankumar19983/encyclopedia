package com.rajankumar.encyclopaedia.feature.accessibility

enum class SpeechState { IDLE, PREPARING, SPEAKING, ERROR }

fun SpeechState.isBusy(): Boolean = this == SpeechState.PREPARING || this == SpeechState.SPEAKING