package com.rajankumar.encyclopaedia.feature.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberTextToSpeechController(): TextToSpeechController {
  val context = LocalContext.current
  val controller = remember(context) { TextToSpeechController(context) }

  DisposableEffect(controller) {
    onDispose { controller.shutdown() }
  }

  return controller
}
